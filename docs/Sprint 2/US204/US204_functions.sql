--GET_SHIPTRIP_ID
create or replace function get_shiptrip_id(f_container_id container.container_id%type, f_shipment_id shipment.shipment_id%type) return integer
is
    f_shiptrip_id shipment.shiptrip_id%type;
begin
    SELECT shiptrip_id into f_shiptrip_id
    FROM shipment
    WHERE container_id = f_container_id AND shipment_id = f_shipment_id;
    return (f_shiptrip_id);

exception
    when no_data_found then
        return -1;
end;

--GET_LOCATION
create or replace function get_location(f_shiptrip_id shiptrip.shiptrip_id%type) return varchar
is
    f_arrival_date date;
    f_departure_date date;
    f_arrival_location  integer;
    f_departure_location integer;
    f_mmsi integer;
    f_locationName varchar(100);
begin
    select real_departure_date, real_arrival_date, arrival_location, departure_location, mmsi
    into f_departure_date, f_arrival_date, f_arrival_location, f_departure_location, f_mmsi
    from shiptrip
    where shiptrip_id = f_shiptrip_id;

    if f_arrival_date IS NOT NULL then  --the container is in the arrival port
        select name into f_locationName from port where port_id=f_arrival_location;
        return ('PORT, '||(f_locationName));
    elsif f_departure_date IS NULL then --the container is in the departure port
        select name into f_locationName from port where port_id=f_departure_location;
        return ('PORT, '||(f_locationName));
    else            --the container is in the ship
        select shipName into f_locationName from ship where mmsi=f_mmsi;
        return ('SHIP, '||(f_locationName));
    end if;

exception
    when no_data_found then
        return ('There is no ship trip for that container and shipment id. Please verify if you inserted your data correctly.');
end;


--TESTS
--Check the Ship Trip ID for Container ID 2116393 and Shipment ID 374
SET SERVEROUTPUT ON;
begin
    dbms_output.put_line('the ship trip is: '||get_shiptrip_id(2549246,374));
end;

--Check the if raises exception and Ship Trip Id = -1 when there isn't data found
SET SERVEROUTPUT ON;
begin
    dbms_output.put_line('the ship trip is: '||get_shiptrip_id(1,2));
end;

--Check if the method works properly
SET SERVEROUTPUT ON;
begin
    dbms_output.put_line('The location id is: '||get_location(16347));
end;

--Check if raises exception
SET SERVEROUTPUT ON;
begin
    dbms_output.put_line('The location id is: '||get_location(2));
end;