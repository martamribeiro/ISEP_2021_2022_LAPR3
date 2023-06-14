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