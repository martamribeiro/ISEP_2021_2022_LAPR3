--US404

--function to check if the ship has been idle on a given day
CREATE OR REPLACE FUNCTION check_ship_idle_day(f_ship_mmsi ship.mmsi%type, f_day_date date) return boolean
IS
   c_real_departure_date date;
   c_real_arrival_date date;
   c_shiptrip_id shiptrip.shiptrip_id%type;

    CURSOR c_trips is
      SELECT shiptrip_id, real_departure_date, real_arrival_date FROM SHIPTRIP WHERE MMSI=f_ship_mmsi;

BEGIN
    OPEN c_trips;
    LOOP
    FETCH c_trips into c_shiptrip_id, c_real_departure_date, c_real_arrival_date;
        EXIT WHEN c_trips%notfound;
        --if the ship had at least a shiptrip where it has left OR arrived in that date OR the date was in the middle of the trip in that date, it means the ship was not idle in that day
        IF (f_day_date >= c_real_departure_date AND f_day_date <= c_real_arrival_date) THEN
            return false;
        END IF;

    END LOOP;
    CLOSE c_trips;

    return true;

    exception
        when no_data_found then
            return false;
END;
/

--function to count the days a ship has been idle since the beginning of the year
CREATE OR REPLACE FUNCTION check_ship_idle_year(f_ship_mmsi ship.mmsi%type, f_start_year date) return integer
IS
    count_days integer := 0;
    current_date date := f_start_year;
    isIdleOnDay boolean;

BEGIN
    WHILE to_char(current_date, 'DD-MM-YYYY') != to_char(sysdate+1, 'DD-MM-YYYY')
    LOOP
        isIdleOnDay := check_ship_idle_day(f_ship_mmsi, current_date);
        IF isIdleOnDay THEN
            count_days := count_days+1;
        END IF;
        current_date := current_date+1;
    END LOOP;

    return count_days;

    exception
        when no_data_found then
            return null;
END;
/

--function to get for each ship the idle days
CREATE OR REPLACE FUNCTION all_ships_idle(f_start_year varchar) return varchar
IS
    c_mmsi  ship.mmsi%type;
    ships varchar(32700);
    idle_days integer := 0;
    start_year date := TO_DATE(f_start_year, 'DD-MM-YYYY');

    CURSOR c_ships is
      SELECT mmsi FROM SHIP;
BEGIN
    OPEN c_ships;
    LOOP
    FETCH c_ships into c_mmsi;
      EXIT WHEN c_ships%notfound;
    idle_days := check_ship_idle_year(c_mmsi, start_year);
    ships := ships || 'Ship MMSI: ' || c_mmsi || ' | Number of Idle Days: ' || idle_days || chr(10);
    END LOOP;
    CLOSE c_ships;

    return ships;

    exception
        when no_data_found then
            return ('No data found');
END;
/

--US405



--////////////////////////////////////////////////////// BEGINNING: US405

--GET SHIP MAX CAPACITY

create or replace function get_ship_max_capacity(f_mmsi ship.mmsi%type) return integer
is

    f_max_capacity integer;

begin

select to_number(currentCapacity) into f_max_capacity
from ship
where mmsi = f_mmsi;

return (f_max_capacity);

exception
        when no_data_found then
        return 0;

end;
/

--//////////////////////////////////////////////////////

--GET NUMBER OF CONTAINER IN A SHIP ON A DAY

create or replace function get_num_containers_ship_day(f_mmsi ship.mmsi%type, f_date shipTrip.real_departure_date%type) return integer
is

    f_current_num integer:=0;
    f_shiptrip_id shipTrip.shiptrip_id%type;
    f_loading_cargo_id shipTrip.loading_cargo_id%type;
    f_unloading_cargo_id shipTrip.unloading_cargo_id%type;

cursor loadingCargos
        is
        (select loading_cargo_id
        from shipTrip
        where mmsi=f_mmsi AND real_departure_date < f_date);

cursor unloadingCargos
        is
        (select unloading_cargo_id
        from shipTrip
        where mmsi=f_mmsi AND real_arrival_date < f_date);

begin

open loadingCargos;
loop
fetch loadingCargos into f_loading_cargo_id;
        exit when loadingCargos%notfound;
        f_current_num := f_current_num + get_num_containers_per_cargoManifest(f_loading_cargo_id);
end loop;

open unloadingCargos;
loop
fetch unloadingCargos into f_unloading_cargo_id;
        exit when unloadingCargos%notfound;
        f_current_num := f_current_num + get_num_containers_per_cargoManifest(f_unloading_cargo_id);
end loop;

return f_current_num;

exception
        when no_data_found then
        return 0;

end;
/

--////////////////////////////////////////////////////// END: US405

--////////////////////////////////////////////////////// BEGINNING: US406

create or replace FUNCTION get_all_voyages RETURN SYS_REFCURSOR
as
    below_threshold SYS_REFCURSOR;


BEGIN
    OPEN below_threshold FOR
    SELECT mmsi, shiptrip_id, departure_location, arrival_location, real_departure_date, real_arrival_date
        FROM SHIPTRIP WHERE real_arrival_date IS NOT NULL;

RETURN (below_threshold);

EXCEPTION
    WHEN no_data_found THEN
        RETURN NULL;
END;
/
--////////////////////////////////////////////////////// END: US406

--US407


--function to get the number of containers per cargo manifest (function reused from another US)
create or replace function get_num_containers_per_cargoManifest(f_cargoManifest_id cargoManifest.cargoManifest_id%type) return integer
is
	f_num_containers_per_cargoManifest integer;
begin
	select count(*) into f_num_containers_per_cargoManifest
	from containerInCargoManifest
	where cargoManifest_id = f_cargoManifest_id;
	return (f_num_containers_per_cargoManifest);
	exception
	when no_data_found then
	return 0;
end;
/

--function to get the containers info belonging to a Cargo Manifest
CREATE OR REPLACE FUNCTION get_containers(f_cargomanifest_id cargoManifest.cargoManifest_id%type) RETURN varchar
IS
    containers_info varchar(32700);
    c_container_id positionInVehicule.container_id%type;
    c_position_x positionInVehicule.containerpositionx%type;
    c_position_y positionInVehicule.containerpositiony%type;
    c_position_z positionInVehicule.containerpositionz%type;

    f_payload container.payload%type;
    f_tare container.tare%type;
    f_gross container.gross%type;
    f_isocode container.isocode%type;

    CURSOR c_positions is
        SELECT container_id, containerpositionx, containerpositiony, containerpositionz FROM positionInVehicule where cargomanifest_id=f_cargomanifest_id;
BEGIN
    containers_info := 'Containers Info: ' || chr(10);
    OPEN c_positions;
    LOOP
    FETCH c_positions into c_container_id, c_position_x, c_position_y, c_position_z;
        EXIT WHEN c_positions%notfound;
        containers_info := containers_info || '>> Container ID: ' || c_container_id || chr(10) || 'Position: (' || c_position_x || ', ' || c_position_y || ', ' || c_position_z || ')' || chr(10);

        SELECT payload, tare, gross, isocode into f_payload, f_tare, f_gross, f_isocode FROM container where container_id=c_container_id;
        containers_info := containers_info || 'Payload: ' || f_payload || ' | Tare: ' || f_tare || ' | Gross: ' || f_gross || ' | ISO Code: ' || f_isocode || chr(10) || chr(10);

    END LOOP;
    CLOSE c_positions;

    return containers_info;

    exception
            when no_data_found then
                return 'No Data Found';
END;
/

--function to get the type of transport of a ship (truck or ship)
CREATE OR REPLACE FUNCTION get_transport(c_transport integer) RETURN varchar
IS
    isShip integer;
BEGIN
    SELECT COUNT(*) INTO isShip FROM SHIP WHERE mmsi=c_transport;
      IF isShip = 0 THEN
        return 'Truck';
      ELSE
        return 'Ship';
      END IF;

      exception
        when no_data_found then
          return 'No Data Found';
END;
/

--function to get the unloading info in a date
CREATE OR REPLACE FUNCTION get_info_unloading_day(f_day_date date) RETURN varchar
IS
    info varchar(32700);
    date_unload date;
    c_transport integer;
    c_est_arrival_date date;
    c_unloading_cargo_id integer;
    cursorIsEmpty boolean := true;

    type_of_transport char(10);

    CURSOR c_trips is
      SELECT mmsi, est_arrival_date, unloading_cargo_id FROM SHIPTRIP WHERE EST_ARRIVAL_DATE=f_day_date
      union all
      SELECT truck_id, est_arrival_date, unloading_cargo_id FROM TRUCKTRIP WHERE EST_ARRIVAL_DATE=f_day_date;

BEGIN
    OPEN c_trips;
    LOOP
    FETCH c_trips into c_transport, c_est_arrival_date, c_unloading_cargo_id;

    If c_trips%NOTFOUND AND cursorIsEmpty THEN
      CLOSE c_trips;
      return 'No Unloading Cargo Manifests for this day';
    ELSE
        cursorIsEmpty := false;
        EXIT WHEN c_trips%notfound;

        type_of_transport := get_transport(c_transport);

        info := info || '> Cargo Manifest ID: ' || c_unloading_cargo_id || chr(10) || 'Unload Date: ' || c_est_arrival_date || chr(10) || 'Transport: ' ||  get_transport(c_transport) || chr(10);
        info := info || 'Number of Containers to Unload: ' || get_num_containers_per_cargoManifest(c_unloading_cargo_id) || chr(10);
        IF type_of_transport = 'Ship' THEN
            info := info || get_containers(c_unloading_cargo_id) || chr(10) || chr(10);
        ELSE
            info := info || get_containers_truck(c_unloading_cargo_id) || chr(10) || chr(10);
        END IF;

    END IF;
    END LOOP;
    CLOSE c_trips;
    return info;

    exception
        when no_data_found then
                return 'No Data Found';
END;
/

--function to get the loading info in a date
CREATE OR REPLACE FUNCTION get_info_loading_day(f_day_date date) RETURN varchar
IS
    info varchar(32700);
    date_load date;
    c_transport integer;
    c_est_departure_date date;
    c_loading_cargo_id integer;
    cursorIsEmpty boolean := true;

    type_of_transport char(10);

    CURSOR c_trips is
      SELECT mmsi, est_departure_date, loading_cargo_id FROM SHIPTRIP WHERE EST_DEPARTURE_DATE=f_day_date
      union all
      SELECT truck_id, est_departure_date, loading_cargo_id FROM TRUCKTRIP WHERE EST_DEPARTURE_DATE=f_day_date;

BEGIN
    OPEN c_trips;
    LOOP
    FETCH c_trips into c_transport, c_est_departure_date, c_loading_cargo_id;

    If c_trips%NOTFOUND AND cursorIsEmpty THEN
      CLOSE c_trips;
      return 'No Loading Cargo Manifests for this day';
    ELSE
        cursorIsEmpty := false;
        EXIT WHEN c_trips%notfound;

        type_of_transport := get_transport(c_transport);

        info := info || '> Cargo Manifest ID: ' || c_loading_cargo_id || chr(10) || 'Load Date: ' || c_est_departure_date || chr(10) || 'Transport: ' || get_transport(c_transport) || chr(10);
        info := info || 'Number of Containers to Load: ' || get_num_containers_per_cargoManifest(c_loading_cargo_id) || chr(10);
        IF type_of_transport = 'Ship' THEN
            info := info || get_containers(c_loading_cargo_id) || chr(10) || chr(10);
        ELSE
            info := info || get_containers_truck(c_loading_cargo_id) || chr(10) || chr(10);
        END IF;
    END IF;
    END LOOP;
    CLOSE c_trips;
    return info;

    exception
            when no_data_found then
                    return 'No Data Found';
END;
/

--function which returns the info of loading and unloading tasks in a day
CREATE OR REPLACE FUNCTION get_loading_unloading_day(f_day_date varchar) RETURN varchar
IS
    day_date date := TO_DATE(f_day_date, 'DD-MM-YYYY');
    info varchar(32700);
BEGIN
    return get_info_unloading_day(day_date) || chr(10) || chr(10) || get_info_loading_day(day_date);
END;
/

--function which returns the info of containers in case it's a truck trip (the containers don't have position in vehicle)
CREATE OR REPLACE FUNCTION get_containers_truck(f_cargomanifest_id cargoManifest.cargoManifest_id%type) RETURN varchar
IS
    containers_info varchar(32700);

    f_payload container.payload%type;
    f_tare container.tare%type;
    f_gross container.gross%type;
    f_isocode container.isocode%type;

    c_container_id container.container_id%type;

     CURSOR c_containers is
        SELECT container_id into c_container_id FROM containerInCargoManifest where cargomanifest_id=f_cargomanifest_id;
BEGIN

    containers_info := 'Containers Info: ' || chr(10);
    OPEN c_containers;
    LOOP
    FETCH c_containers into c_container_id;
        EXIT WHEN c_containers%notfound;
        containers_info := containers_info || '>> Container ID: ' || c_container_id || chr(10);

        SELECT payload, tare, gross, isocode into f_payload, f_tare, f_gross, f_isocode FROM container where container_id=c_container_id;
        containers_info := containers_info || 'Payload: ' || f_payload || ' | Tare: ' || f_tare || ' | Gross: ' || f_gross || ' | ISO Code: ' || f_isocode || chr(10) || chr(10);

    END LOOP;
    CLOSE c_containers;

    return containers_info;

    exception
            when no_data_found then
                    return 'No Data Found';
END;


