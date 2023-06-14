--FOR US208 AND US209:

insert into ship (mmsi, vesseltypeid, imo, callsign, shipname, currentcapacity, draft, length, width) values (212351001, 70, 'IMO9305686', '5BZP4', 'HYUNDAI SINGAPORA', '79', 14.5, 303, 40);
insert into ship (mmsi, vesseltypeid, imo, callsign, shipname, currentcapacity, draft, length, width) values (636092933, 79, 'IMO9225642', 'D5VK7', 'MSC ILONO', '79', 11.8, 299, 40);

insert into cargomanifest (cargomanifest_id) values (19822);
insert into cargomanifest (cargomanifest_id) values (724);
insert into cargomanifest (cargomanifest_id) values (39824);
insert into cargomanifest (cargomanifest_id) values (45807);
insert into cargomanifest (cargomanifest_id) values (77329);
insert into cargomanifest (cargomanifest_id) values (87508);

insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (8234084, 19822, null);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (628900, 19822, -18);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (2183409, 19822, 20);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (8561707, 19822, null);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (2650387, 19822, -26);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (7749375, 19822, 11);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (9661114, 19822, null);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (7857665, 19822, 6);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (8889089, 19822, -28);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (4107082, 19822, 21);

insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (5351520, 724, null);

insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (3705156, 39824, -11);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (3753096, 39824, null);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (4162565, 39824, 24);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (6807391, 39824, null);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (5308377, 39824, -11);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (6956397, 39824, -17);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (4074022, 39824, -4);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (6915770, 39824, 21);

insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (2353883, 45807, null);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (3011850, 45807, null);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (4847076, 45807, -7);

insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (123768, 77329, 15);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (1558071, 77329, null);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (896255, 77329, 19);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (7329290, 77329, 7);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (140768, 77329, null);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (7827555, 77329, -15);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (3951071, 77329, 8);

insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (9803333, 87508, null);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (5734111, 87508, -12);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (4661451, 87508, -13);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (5577067, 87508, -5);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (7104943, 87508, 20);
insert into containerincargomanifest (container_id, cargomanifest_id, temperature_kept) values (961837, 87508, -9);

insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (81341, 212351000, 224858, 16485, 19822, 724, '06/02/2021', '08/02/2021', '26/02/2021', '08/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (67131, 212351000, 13012, 11771, 39824, 45807, '11/02/2021', '20/02/2021', '11/03/2021', '17/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (68138, 212351000, 18867, 24795, 77329, 87508, '24/02/2021', '26/02/2021', '27/03/2021', '26/04/2021');

--END OF INSERTS FOR US208 AND US209.