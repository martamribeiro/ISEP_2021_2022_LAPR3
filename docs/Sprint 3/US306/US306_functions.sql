--GET NUM CONTAINERS OUT WAREHOUSE NEXT 30 DAYS
create or replace function get_num_containers_out_warehouse(f_warehouse_id warehouse.warehouse_id%type,f_currentDate truckTrip.est_departure_date%type, f_finalDate truckTrip.est_departure_date%type) return integer
is
f_comp_manifest cargoManifest.cargoManifest_id%type;
f_num_containers_out_warehouse integer:=0;
cursor desiredManifests
is
(select loading_cargo_id from trucktrip where departure_location=(select location_id from warehouse where warehouse_id=f_warehouse_id)
AND est_departure_date > f_currentDate and est_departure_date <= f_finalDate);
begin
open desiredManifests;
loop
fetch desiredManifests into f_comp_manifest;
exit when desiredManifests%notfound;
f_num_containers_out_warehouse:=f_num_containers_out_warehouse+get_num_containers_per_cargoManifest(f_comp_manifest);
end loop;
return (f_num_containers_out_warehouse);
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

------------------------------------------------------------REUSED------------------------------------------------------------
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