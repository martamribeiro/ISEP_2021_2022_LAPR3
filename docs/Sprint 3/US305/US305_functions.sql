--get_route_id
create or replace function get_route_id(f_container_id container.container_id%type, f_registration_code client.registration_code%type) return varchar
is
    container_exists integer;
    not_leased_client integer;
    f_route_id route.route_id%type;

    ex_invalid_container_id exception;
    ex_not_leased_client exception;
begin

    select count(*) into container_exists from container where container_id=f_container_id;

    if container_exists = 0 then
        raise ex_invalid_container_id;
    end if;

    select count(*) into not_leased_client from shipment where container_id=f_container_id AND registration_code=f_registration_code;

    if not_leased_client = 0 then
        raise ex_not_leased_client;
    end if;

    select route_id into f_route_id from shipment where container_id=f_container_id AND registration_code=f_registration_code;

    return f_route_id;

exception
    when ex_invalid_container_id then
        return '10 – invalid container id';
    when ex_not_leased_client then
        return '11 – container is not leased by client';
    when others then
        return 'Invalid data';
end;
/

--get_path_function
CREATE OR REPLACE FUNCTION get_path_function(f_route_id route.route_id%type) return varchar
IS
   c_departure_location integer;
   c_arrival_location integer;
   c_est_departure_date date;
   c_est_arrival_date date;
   c_real_departure_date date;
   c_real_arrival_date date;
   c_transport integer;

   departure_location varchar(50);
   arrival_location varchar(50);
   type_of_transport varchar(20);

   isDeparturePort integer;
   isArrivalPort integer;
   isShip integer;

   route varchar(32700);

   CURSOR c_trips is
      SELECT mmsi, departure_location, arrival_location, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date FROM SHIPTRIP WHERE ROUTE_ID=f_route_id
      union all
      SELECT truck_id, departure_location, arrival_location, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date FROM TRUCKTRIP WHERE ROUTE_ID=f_route_id
      ORDER BY EST_DEPARTURE_DATE;
BEGIN
   OPEN c_trips;
   LOOP
   FETCH c_trips into c_transport, c_departure_location, c_arrival_location, c_est_departure_date, c_est_arrival_date, c_real_departure_date, c_real_arrival_date;
      EXIT WHEN c_trips%notfound;

      SELECT COUNT(*) INTO isShip FROM SHIP WHERE mmsi=c_transport;
      IF isShip = 0 THEN
        type_of_transport := 'Truck';
      ELSE
        type_of_transport := 'Ship';
      END IF;

      IF type_of_transport = 'Ship' THEN --if the type of transport is a ship then the arrival and departure location must be a Port
        SELECT name INTO departure_location FROM PORT where port_id=c_departure_location;
        departure_location := 'Port ' || departure_location;
        SELECT name INTO arrival_location FROM PORT where port_id=c_arrival_location;
        arrival_location := 'Port ' || arrival_location;
      ELSE --if the type of transport is a truck
        --there are 2 options:
        --if the departure location is a Port, then the arrival location must be a Warehouse;
        --if the departure location is a Warehouse, then arrival location can be either a Port OR Warehouse.
        SELECT COUNT(*) INTO isDeparturePort FROM PORT WHERE location_id=c_departure_location;
        IF isDeparturePort = 0 THEN --the departure location is a Warehouse
            SELECT name INTO departure_location FROM WAREHOUSE where location_id=c_departure_location;
            departure_location := 'Warehouse ' || departure_location;
            --check whether arrival location is a Warehouse or Port
            SELECT COUNT(*) INTO isArrivalPort FROM PORT WHERE location_id=c_arrival_location;
            IF isArrivalPort = 0 THEN
                SELECT name INTO arrival_location FROM WAREHOUSE where location_id=c_arrival_location;
                arrival_location := 'Warehouse ' || arrival_location;
            ELSE
                SELECT name INTO arrival_location FROM PORT where location_id=c_arrival_location;
                arrival_location := 'Port ' || arrival_location;
            END IF;

        ELSE --the departure location is a Port, therefore the arrival location must be a Warehouse
            SELECT name INTO departure_location FROM PORT where location_id=c_departure_location;
            departure_location := 'Port ' || departure_location;
            SELECT name INTO arrival_location FROM WAREHOUSE where location_id=c_arrival_location;
            arrival_location := 'Warehouse ' || arrival_location;
        END IF;

      END IF;

      IF c_real_departure_date IS NULL THEN --the container is at the location
        route := route || '  > Current Container Location: ' || departure_location || ' < ';
        return route;
      ELSE --the container is NOT in the location
        route := route || 'Departure Location: ' || departure_location || ', Departure Date: ' || c_real_departure_date;
        IF c_real_arrival_date IS NOT NULL THEN
            route := route || ', Arrival Location: ' || arrival_location || ', Arrival Date: ' || c_real_arrival_date;
        ELSE --the transport hasn't arrived to the location
            route := route || chr(10) || '  > The ' || type_of_transport || ' is between ' || departure_location || ' and ' || arrival_location || ' < ';
            return route;
        END IF;
      END IF;
      route := route || ', Transport: ' || type_of_transport || chr(10);
   END LOOP;
   CLOSE c_trips;
   --the container has arrived its final destination
   route := route || '  > Container has arrived at its final destination at ' || arrival_location || ' < ';
   return route;

   exception
    when no_data_found then
        return ('There is no route id defined for that container and registration code. Please verify if you inserted your data correctly.');
END;
/

--TESTS -> get_route_id

--the container id is invalid, therefore raises ex_invalid_container_id exception
SET SERVEROUTPUT ON;
begin
    dbms_output.put_line('The route id is: '||get_route_id(12,2));
end;

--the container id is valid, but it was not leased by client, therefore raises ex_not_leased_client exception
SET SERVEROUTPUT ON;
begin
    dbms_output.put_line('The route id is: '||get_route_id(9803333,2));
end;

--the container id is valid AND leased by the client, therefore it doesn't raise exception and returns route id
SET SERVEROUTPUT ON;
begin
    dbms_output.put_line('The route id is: '||get_route_id(9803333,6));
end;

--TESTS -> get_path_function

--FIRST SITUATION: the container is at the a location
SET SERVEROUTPUT ON;
begin
    dbms_output.put_line(get_path_function(7));
end;

--SECOND SITUATION: the container is in the middle of the ocean
SET SERVEROUTPUT ON;
begin
    dbms_output.put_line(get_path_function(8));
end;

--THIRD SITUATION: the container is in a truck
SET SERVEROUTPUT ON;
begin
    dbms_output.put_line(get_path_function(9));
end;

--FOURTH SITUATION: the container has arrived its destination
SET SERVEROUTPUT ON;
begin
    dbms_output.put_line(get_path_function(10));
end;
