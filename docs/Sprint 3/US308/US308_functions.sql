--TRIGGER FOR NUMBER OF CONTAINERS GREATER THAN MAX NUMBER OF CONTAINERS
create or replace trigger trgContainers
before insert or update on ShipTrip
                            for each row
declare
f_shiptrip_id shiptrip.shiptrip_id%type;
f_cargoManifest_id cargomanifest.cargomanifest_id%type;
f_mmsi shipTrip.mmsi%type;
f_estDepDate shipTrip.est_departure_date%type;
f_containers_before integer;
f_containers_max integer;
f_containers_after integer;
begin
f_shiptrip_id:= :new.shiptrip_id;
f_cargoManifest_id:= :new.loading_cargo_id;
f_mmsi:= :new.mmsi;
f_estDepDate:= :new.est_departure_date;
select currentCapacity into f_containers_max from Ship where mmsi=f_mmsi;
f_containers_before:=get_initial_num_containers_per_ship_trip(f_cargoManifest_id,f_estDepDate,f_mmsi);
f_containers_after:=f_containers_before+get_num_containers_per_cargomanifest(f_cargomanifest_id);
if f_containers_after>f_containers_max then
raise_application_error(-20001,'Currently, the ship doesnt have enough capacity for the cargo manifest.');
end if;
end;
/

--CHECK IF SHIP TRIP EXISTS
create or replace function check_if_shipTrip_exists(f_shipTrip_id shipTrip.shipTrip_id%type) return integer
is
f_result integer;
begin
select count(*) into f_result
from shipTrip
where shipTrip_id = f_shipTrip_id;
return (f_result);
exception
when no_data_found then
return 0;
end;
/

--DELETE SHIP TRIP
create or replace function delete_shipTrip
(f_shiptrip_id shiptrip.shiptrip_id%type) return integer
is
begin
delete
from shipTrip
where
        shiptrip_id = f_shiptrip_id;
return 1;
exception
when no_data_found then
return -1;
end;
/

--CREATE SHIP TRIP
create or replace function create_shipTrip
(f_shiptrip_id shiptrip.shiptrip_id%type, f_route_id route.route_id%type, f_mmsi shiptrip.mmsi%type, f_departure_location shiptrip.departure_location%type,
f_arrival_location shiptrip.arrival_location%type, f_loading_cargo_id shiptrip.loading_cargo_id%type,
f_est_departure_date shiptrip.est_departure_date%type, f_est_arrival_date shiptrip.est_arrival_date%type) return integer
is
f_check integer;
f_check2 integer;
begin
f_check:=check_if_ship_exists(f_mmsi);
if f_check=0 then
return -1;
end if;
f_check2:=check_if_cargoManifest_exists(f_loading_cargo_id);
if f_check2=0 then
return -1;
end if;
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (f_shiptrip_id, f_route_id, f_mmsi, f_departure_location, f_arrival_location, f_loading_cargo_id, NULL, f_est_departure_date, f_est_arrival_date, NULL, NULL);
return 1;
exception
when no_data_found then
return -1;
end;
/

--GET REAL DEPARTURE DATE FROM SHIP TRIP
create or replace function get_real_departure_date_from_ship_trip(f_cargoManifest_id cargoManifest.cargoManifest_id%type) return shipTrip.real_departure_date%type
is
f_shiptrip_id shipTrip.shiptrip_id%type;
f_real_departure_date shipTrip.real_departure_date%type;
begin
select shiptrip_id into f_shiptrip_id
from shipTrip
where loading_cargo_id = f_cargoManifest_id OR unloading_cargo_id = f_cargoManifest_id;
select real_departure_date into f_real_departure_date
from shipTrip
where shiptrip_id = f_shiptrip_id;
return f_real_departure_date;
end;
/

--GET INITIAL NUM CONTAINERS PER SHIP TRIP WITH REAL DATES
create or replace function get_initial_num_containers_per_ship_trip_2(f_cargoManifest_id cargoManifest.cargoManifest_id%type,
f_real_departure_date shipTrip.real_departure_date%type, f_mmsi ship.mmsi%type) return integer
is
f_comp_loading_cargo_id cargoManifest.cargoManifest_id%type;
f_comp_unloading_cargo_id cargoManifest.cargoManifest_id%type;
f_initial_num_containers_per_ship_trip integer:=0;
cursor neededShipTrips
is
(select loading_cargo_id, unloading_cargo_id
from shipTrip
where mmsi=f_mmsi AND real_departure_date < f_real_departure_date);
begin
open neededShipTrips;
loop
fetch neededShipTrips into f_comp_loading_cargo_id, f_comp_unloading_cargo_id;
exit when neededShipTrips%notfound;
f_initial_num_containers_per_ship_trip := f_initial_num_containers_per_ship_trip + get_num_containers_per_cargoManifest(f_comp_loading_cargo_id) - get_num_containers_per_cargoManifest(f_comp_unloading_cargo_id);
end loop;
return f_initial_num_containers_per_ship_trip;
exception
when no_data_found then
return 0;
end;
/

---------------------------------------------------------------------------REUSING US209:---------------------------------------------------------------------------

--CHECK IF CARGO MANIFEST EXISTS
create or replace function check_if_cargoManifest_exists(f_cargoManifest_id cargomanifest.cargomanifest_id%type) return integer
is
f_result integer;
begin
select count(*) into f_result
from cargomanifest
where cargomanifest_id = f_cargomanifest_id;
return (f_result);
exception
when no_data_found then
return 0;
end;
/

--GET CARGO MANIFEST BY MMSI AND DATE
create or replace function get_cargo_manifest_by_mmsi_and_date(f_mmsi shipTrip.mmsi%type, f_date shipTrip.est_departure_date%type) return cargoManifest.cargoManifest_id%type
is
f_shiptrip_id shipTrip.shiptrip_id%type;
f_unloading_cargo_id cargoManifest.cargoManifest_id%type;
f_est_departure_date shipTrip.est_departure_date%type;
f_est_arrival_date shipTrip.est_arrival_date%type;
f_cargoManifest_id cargoManifest.cargoManifest_id%type;
begin
select shiptrip_id, unloading_cargo_id, est_departure_date, est_arrival_date, loading_cargo_id
into f_shiptrip_id, f_unloading_cargo_id, f_est_departure_date, f_est_arrival_date, f_cargoManifest_id
from
    (select * from
        (select * from shipTrip
         where mmsi=f_mmsi AND est_departure_date<=f_date)
     order by est_departure_date desc)
where rownum=1;
if f_est_arrival_date <= f_date then
select unloading_cargo_id into f_cargoManifest_id
from shipTrip
where shiptrip_id=f_shiptrip_id;
end if;
return (f_cargoManifest_id);
exception
when no_data_found then
return -1;
end;
/

---------------------------------------------------------------------------REUSING US208:---------------------------------------------------------------------------

--CHECK IF SHIP EXISTS
create or replace function check_if_ship_exists(f_mmsi ship.mmsi%type) return integer
is
f_result integer;
begin
select count(*) into f_result
from ship
where mmsi = f_mmsi;
return (f_result);
exception
when no_data_found then
return 0;
end;
/

--GET ADDED AND REMOVED CONTAINERS IN A SHIP TRIP FOR A MOMENT
create or replace function get_added_removed_containers_ship_trip_moment(f_cargoManifest_id cargoManifest.cargoManifest_id%type) return integer --est date como parametro
is
f_shiptrip_id shipTrip.shiptrip_id%type;
f_loading_cargo_id shipTrip.loading_cargo_id%type;
f_unloading_cargo_id shipTrip.unloading_cargo_id%type;
f_num_added_removed_containers_ship_trip_moment integer:=0;
begin
select shiptrip_id into f_shiptrip_id
from shipTrip
where loading_cargo_id=f_cargoManifest_id or unloading_cargo_id=f_cargoManifest_id;
select loading_cargo_id, unloading_cargo_id into f_loading_cargo_id, f_unloading_cargo_id
from shipTrip
where shiptrip_id = f_shiptrip_id;
f_num_added_removed_containers_ship_trip_moment := f_num_added_removed_containers_ship_trip_moment + get_num_containers_per_cargoManifest(f_loading_cargo_id);
if f_unloading_cargo_id=f_cargoManifest_id then
f_num_added_removed_containers_ship_trip_moment := f_num_added_removed_containers_ship_trip_moment + get_num_containers_per_cargoManifest(f_unloading_cargo_id);
end if;
return f_num_added_removed_containers_ship_trip_moment;
exception
when no_data_found then
return 0;
end;
/

--GET EST DEPARTURE DATE FROM SHIP TRIP
create or replace function get_est_departure_date_from_ship_trip(f_cargoManifest_id cargoManifest.cargoManifest_id%type) return shipTrip.est_departure_date%type
is
f_shiptrip_id shipTrip.shiptrip_id%type;
f_est_departure_date shipTrip.est_departure_date%type;
begin
select shiptrip_id into f_shiptrip_id
from shipTrip
where loading_cargo_id = f_cargoManifest_id OR unloading_cargo_id = f_cargoManifest_id;
select est_departure_date into f_est_departure_date
from shipTrip
where shiptrip_id = f_shiptrip_id;
return f_est_departure_date;
end;
/

--GET INITIAL NUM CONTAINERS PER SHIP TRIP
create or replace function get_initial_num_containers_per_ship_trip(f_cargoManifest_id cargoManifest.cargoManifest_id%type,
f_est_departure_date shipTrip.est_departure_date%type, f_mmsi ship.mmsi%type) return integer
is
f_comp_loading_cargo_id cargoManifest.cargoManifest_id%type;
f_comp_unloading_cargo_id cargoManifest.cargoManifest_id%type;
f_initial_num_containers_per_ship_trip integer:=0;
cursor neededShipTrips
is
(select loading_cargo_id, unloading_cargo_id
from shipTrip
where mmsi=f_mmsi AND est_departure_date < f_est_departure_date);
begin
open neededShipTrips;
loop
fetch neededShipTrips into f_comp_loading_cargo_id, f_comp_unloading_cargo_id;
exit when neededShipTrips%notfound;
f_initial_num_containers_per_ship_trip := f_initial_num_containers_per_ship_trip + get_num_containers_per_cargoManifest(f_comp_loading_cargo_id) - get_num_containers_per_cargoManifest(f_comp_unloading_cargo_id);
end loop;
return f_initial_num_containers_per_ship_trip;
exception
when no_data_found then
return 0;
end;
/

--GET SHIP MAX CAPACITY
create or replace function get_max_capacity(f_cargoManifest_id cargoManifest.cargoManifest_id%type) return integer
is
f_mmsi shipTrip.mmsi%type;
f_max_capacity integer;
begin
select mmsi into f_mmsi
from shipTrip
where loading_cargo_id = f_cargoManifest_id OR unloading_cargo_id = f_cargoManifest_id;
select to_number(currentCapacity) into f_max_capacity
from ship
where mmsi = f_mmsi;
return (f_max_capacity);
exception
when no_data_found then
return 0;
end;
/

--GET MMSI BY CARGO MANIFEST ID
create or replace function get_mmsi_by_cargo_manifest_id(f_cargoManifest_id cargoManifest.cargoManifest_id%type) return integer --est date como parametro
is
f_shiptrip_id shipTrip.shiptrip_id%type;
f_mmsi ship.mmsi%type;
begin
select shiptrip_id into f_shiptrip_id
from shipTrip
where loading_cargo_id=f_cargoManifest_id or unloading_cargo_id=f_cargoManifest_id;
select mmsi into f_mmsi
from shipTrip
where shiptrip_id = f_shiptrip_id;
return f_mmsi;
exception
when no_data_found then
return -1;
end;
/

--GET NUM CONTAINERS PER CARGO MANIFEST
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