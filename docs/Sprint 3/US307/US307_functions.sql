--TRIGGER FOR NUMBER OF CONTAINERS UNLOADING MANIFEST BECOMES GREATER THAN MAX NUMBER OF CONTAINERS WAREHOUSE
create or replace trigger trgContainersWarehouse
before insert or update on TruckTrip
                            for each row
declare
f_trucktrip_id trucktrip.trucktrip_id%type;
f_cargoManifest_id cargomanifest.cargomanifest_id%type;
f_arrival_location truckTrip.arrival_location%type;
f_warehouse_id warehouse.warehouse_id%type;
f_estArrDate truckTrip.est_arrival_date%type;
f_containers_before integer;
f_containers_max integer;
f_containers_after integer;
begin
f_trucktrip_id:= :new.trucktrip_id;
f_cargoManifest_id:= :new.unloading_cargo_id;
f_arrival_location:= :new.arrival_location;
select warehouse_id into f_warehouse_id from warehouse where location_id=f_arrival_location;
f_estArrDate:= :new.est_arrival_date;
f_containers_before:=get_current_containers_warehouse(f_warehouse_id,f_estArrDate);
select maxCapacity into f_containers_max from Warehouse where warehouse_id=f_warehouse_id;
f_containers_after:=f_containers_before+get_num_containers_per_cargomanifest(f_cargomanifest_id);
if f_containers_after>f_containers_max then
raise_application_error(-20030,'Currently, the destiny warehouse doesnt have enough capacity for the containers in the unloading cargo manifest.');
end if;
end;

/

--CREATE TRUCK TRIP WITH UNLOADING CARGO MANIFEST
create or replace function create_truckTrip_with_unloading
(f_trucktrip_id trucktrip.trucktrip_id%type, f_route_id route.route_id%type, f_truck_id trucktrip.truck_id%type, f_departure_location trucktrip.departure_location%type,
f_arrival_location trucktrip.arrival_location%type, f_loading_cargo_id trucktrip.loading_cargo_id%type, f_unloading_cargo_id trucktrip.unloading_cargo_id%type,
f_est_departure_date trucktrip.est_departure_date%type, f_est_arrival_date trucktrip.est_arrival_date%type) return integer
is
f_check integer;
f_check2 integer;
f_check3 integer;
begin
f_check:=check_if_truck_exists(f_truck_id);
if f_check=0 then
return -1;
end if;
f_check2:=check_if_cargoManifest_exists(f_loading_cargo_id);
if f_check2=0 then
return -1;
end if;
f_check3:=check_if_cargoManifest_exists(f_unloading_cargo_id);
if f_check3=0 then
return -1;
end if;
insert into trucktrip (trucktrip_id, route_id, truck_id, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (f_trucktrip_id, f_route_id, f_truck_id, f_departure_location, f_arrival_location, f_loading_cargo_id, f_unloading_cargo_id, f_est_departure_date, f_est_arrival_date, NULL, NULL);
return 1;
exception
when no_data_found then
return -1;
end;
/

--DELETE TRUCK TRIP - IMPORTADO
create or replace function delete_truckTrip
(f_trucktrip_id trucktrip.trucktrip_id%type) return integer
is
begin
delete
from truckTrip
where
trucktrip_id = f_trucktrip_id;
return 1;
exception
when no_data_found then
return -1;
end;
/

--CHECK IF TRUCK TRIP EXISTS - IMPORTADO
create or replace function check_if_truckTrip_exists(f_truckTrip_id truckTrip.truckTrip_id%type) return integer
is
f_result integer;
begin
select count(*) into f_result
from truckTrip
where truckTrip_id = f_truckTrip_id;
return (f_result);
exception
when no_data_found then
return 0;
end;
/

--CHECK IF TRUCK EXISTS
create or replace function check_if_truck_exists(f_truck_id truck.truck_id%type) return integer
is
f_result integer;
begin
select count(*) into f_result
from truck
where truck_id = f_truck_id;
return (f_result);
exception
when no_data_found then
return 0;
end;
/

---------------------------------------------------------------------------REUSES:---------------------------------------------------------------------------

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

--CHECK IF WAREHOUSE EXISTS
create or replace function check_if_warehouse_exists(f_warehouse_id warehouse.warehouse_id%type) return integer
is
f_result integer;
begin
select count(*) into f_result
from warehouse
where warehouse_id = f_warehouse_id;
return (f_result);
exception
when no_data_found then
return 0;
end;
/

--GET CURRENT CONTAINERS WAREHOUSE
create or replace function get_current_containers_warehouse(f_warehouse_id warehouse.warehouse_id%type, f_current_date shipTrip.est_departure_date%type) return integer
is
f_comp_cargo_id cargoManifest.cargoManifest_id%type;
f_current_containers integer:=0;
cursor neededCargosPut
is
(select unloading_cargo_id
from truckTrip
where arrival_location=(select location_id from warehouse where warehouse_id=f_warehouse_id) AND est_arrival_date < f_current_date);
cursor neededCargosTake
is
(select loading_cargo_id
from truckTrip
where departure_location=(select location_id from warehouse where warehouse_id=f_warehouse_id) AND est_departure_date < f_current_date);
begin
open neededCargosPut;
loop
fetch neededCargosPut into f_comp_cargo_id;
exit when neededCargosPut%notfound;
f_current_containers:= f_current_containers + get_num_containers_per_cargoManifest(f_comp_cargo_id);
end loop;
open neededCargosTake;
loop
fetch neededCargosTake into f_comp_cargo_id;
exit when neededCargosTake%notfound;
f_current_containers:= f_current_containers - get_num_containers_per_cargoManifest(f_comp_cargo_id);
end loop;
return f_current_containers;
exception
when no_data_found then
return 0;
end;
/

--GET WAREHOUSE MAX CAPACITY
create or replace function get_warehouse_max_capacity(f_warehouse_id warehouse.warehouse_id%type) return integer
is
f_max_capacity integer;
begin
select maxCapacity into f_max_capacity
from warehouse
where warehouse_id = f_warehouse_id;
return (f_max_capacity);
exception
when no_data_found then
return 0;
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