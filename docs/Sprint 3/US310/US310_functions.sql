--CHECK PORT OCCUPANCY FOR A GIVEN DAY

create or replace function get_current_num_containers_port(f_port_id port.port_id%type,
f_date shipTrip.est_departure_date%type) return integer
is
f_current_num integer:=0;
f_comp_cargo_id cargoManifest.cargoManifest_id%type;
cursor cursorShipTripLoading
is
(select loading_cargo_id
from shipTrip
where departure_location=f_port_id AND real_departure_date < f_date);
cursor cursorShipTripUnloading
is
(select unloading_cargo_id
from shipTrip
where arrival_location=f_port_id AND real_arrival_date < f_date);
cursor cursorTruckTripLoading
is
(select loading_cargo_id
from truckTrip
where departure_location=(select location_id from port where port_id=f_port_id) AND real_departure_date < f_date);
cursor cursorTruckTripUnloading
is
(select unloading_cargo_id
from truckTrip
where arrival_location=(select location_id from port where port_id=f_port_id) AND real_arrival_date < f_date);
begin
open cursorShipTripUnloading;
loop
fetch cursorShipTripUnloading into f_comp_cargo_id;
exit when cursorShipTripUnloading%notfound;
f_current_num:=f_current_num+get_num_containers_per_cargomanifest(f_comp_cargo_id);
end loop;
open cursorTruckTripUnloading;
loop
fetch cursorTruckTripUnloading into f_comp_cargo_id;
exit when cursorTruckTripUnloading%notfound;
f_current_num:=f_current_num+get_num_containers_per_cargomanifest(f_comp_cargo_id);
end loop;
open cursorShipTripLoading;
loop
fetch cursorShipTripLoading into f_comp_cargo_id;
exit when cursorShipTripLoading%notfound;
f_current_num:=f_current_num-get_num_containers_per_cargomanifest(f_comp_cargo_id);
end loop;
open cursorTruckTripLoading;
loop
fetch cursorTruckTripLoading into f_comp_cargo_id;
exit when cursorTruckTripLoading%notfound;
f_current_num:=f_current_num-get_num_containers_per_cargomanifest(f_comp_cargo_id);
end loop;
return f_current_num;
exception
when no_data_found then
return 0;
end;
/

--GET PORT MAX CAPACITY

create or replace function get_port_max_capacity(f_port_id port.port_id%type) return integer
is
f_max_capacity integer;
begin
select maxCapacity into f_max_capacity
from port
where port_id = f_port_id;
return (f_max_capacity);
exception
when no_data_found then
return 0;
end;
/


--CHECK IF PORT EXISTS

create or replace function check_if_port_exists(f_port_id port.port_id%type) return integer
is
f_result integer;
begin
select count(*) into f_result
from port
where port_id = f_port_id;
return (f_result);
exception
when no_data_found then
return 0;
end;
/