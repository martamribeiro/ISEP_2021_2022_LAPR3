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
/