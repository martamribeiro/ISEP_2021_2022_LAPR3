create or replace function get_route_id(f_container_id container.container_id%type, f_route_id integer) return integer
is
    container_exists integer;
    ex_invalid_container_id exception;
    ex_not_leased_client exception;
begin


    select case
        when not exists(select container_id from container where container_id = f_container_id)
        then 1
        else 0
        end into container_exists
    from dual;

    if container_exists = 0 then
        raise ex_invalid_container_id;
    else
        return 1;
    end if;

exception
    when ex_invalid_container_id then
        dbms_output.put_line('10 – invalid container id');
end;



create or replace function get_route_id(f_container_id container.container_id%type, f_route_id integer) return integer
is
    container_exists integer;
    ex_invalid_container_id exception;
    ex_not_leased_client exception;
begin


    select count(*) into container_exists from container where container_id=f_container_id;

    if container_exists = 0 then
        raise ex_invalid_container_id;
    else
        return 1;
    end if;


exception
    when ex_invalid_container_id then
        return -1;
end;


insert into route(route_id) values(1);
insert into route(route_id) values(2);
insert into route(route_id) values(3);
insert into route(route_id) values(4);
insert into route(route_id) values(5);

--FIRST SITUATION

--insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (95954, 249047000, 10136, 29749, 62698, 86476, '03/03/2021', '12/05/2021', '11/03/2021', '04/05/2021');
--insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (47433, 303221000, 18012, 28313, 19821, 57650, '16/03/2021', '29/05/2021', '03/03/2021', '07/05/2021');
--insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (83011, 249047000, 24795, 14113, 9062, 30009, '23/02/2021', '28/05/2021', '20/03/2021', '05/04/2021');
--insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (68138, 212351000, 18867, 24795, 77328, 87507, '24/02/2021', '17/05/2021', '27/03/2021', '26/04/2021');
--insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (85922, 235092459, 18433, 24795, 34893, 16358, '31/03/2021', '10/05/2021', '12/02/2021', '16/04/2021');

--SECOND SITUATION
--insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (29045, 228339600, 14459, 18137, 77328, 25442, '14/02/2021', '27/05/2021', '11/02/2021', '06/05/2021');
--insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (10710, 210950000, 216592, 22522, 86476, 86476, '27/03/2021', '22/05/2021', '09/02/2021', '20/04/2021');
--insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (59732, 303267000, 13176, 25007, 80342, 89260, '23/02/2021', '23/06/2021', '18/03/2021', '07/05/2021');
--insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (23183, 210950000, 29239, 23247, 66729, 2025, '24/02/2021', '23/06/2021', '17/03/2021', '25/05/2021');
--insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (23255, 303267000, 27248, 17941, 67603, 93962, '04/02/2021', '18/04/2021', '05/02/2021', '25/04/2021');

--THIRD SITUATION
--insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (56778, 229767000, 14113, 18433, 73258, 62698, '26/02/2021', '09/04/2021', '01/03/2021', '09/06/2021');
--insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (74590, 636091400, 17386, 20826, 12366, 3598, '28/02/2021', '13/06/2021', '25/02/2021', '06/06/2021');
--insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (7326, 229767000, 13176, 21451, 53304, 45806, '12/03/2021', '21/06/2021', '11/03/2021', '15/05/2021');
--insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (92307, 256888000, 14459, 23428, 12366, 78991, '19/02/2021', '06/04/2021', '03/02/2021', '15/06/2021');

--FOURTH SITUATION
--insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (5468, 257881000, 18137, 21556, 80342, 9062, '17/03/2021', '03/05/2021', '14/03/2021', '11/04/2021');
--insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (70007, 258692000, 21556, 11174, 53304, 69477, '03/03/2021', '13/06/2021', '02/03/2021', '02/04/2021');
--insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (34710, 636019825, 13012, 27792, 45228, 73258, '31/03/2021', '08/06/2021', '02/02/2021', '16/05/2021');

--us407
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (74072, 303267000, 29239, 22522, 52328, 723, '05/03/2021', '22/05/2021', '21/02/2021', '29/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (65759, 258692000, 21206, 18476, 79225, 77328, '07/02/2021', '14/06/2021', '23/03/2021', '07/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (55009, 256888000, 18937, 10563, 9062, 25442, '06/02/2021', '16/06/2021', '15/02/2021', '14/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (14734, 303221000, 25007, 30045, 66729, 34491, '12/02/2021', '26/04/2021', '21/03/2021', '01/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (47538, 636091400, 17941, 20072, 83244, 93962, '16/02/2021', '13/04/2021', '18/03/2021', '14/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (97997, 258692000, 21381, 21206, 77328, 86476, '07/02/2021', '04/04/2021', '29/03/2021', '26/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (7282, 212351000, 21852, 18937, 54091, 78548, '01/03/2021', '08/06/2021', '10/03/2021', '07/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (9474, 636091400, 20072, 18867, 53304, 69477, '13/02/2021', '24/04/2021', '04/03/2021', '30/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (79138, 256888000, 17941, 21556, 53304, 89260, '07/02/2021', '21/05/2021', '09/02/2021', '03/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (69693, 235092459, 29876, 24795, 54561, 77328, '27/03/2021', '26/04/2021', '21/02/2021', '06/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (29730, 212180000, 13176, 13390, 73258, 54091, '01/02/2021', '26/04/2021', '08/03/2021', '29/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (6843, 258692000, 14226, 224858, 9004, 93962, '24/02/2021', '30/05/2021', '09/02/2021', '11/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (68973, 636019825, 20351, 20847, 96922, 37588, '06/03/2021', '02/04/2021', '15/02/2021', '27/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (18507, 256888000, 18454, 13176, 69477, 73258, '08/03/2021', '26/05/2021', '14/02/2021', '14/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (69451, 303221000, 29239, 10860, 86476, 57650, '17/03/2021', '30/05/2021', '23/02/2021', '07/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (75733, 210950000, 20512, 18200, 66729, 78548, '12/02/2021', '16/04/2021', '22/02/2021', '11/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (26085, 303267000, 21206, 21852, 45228, 79225, '10/03/2021', '13/04/2021', '21/03/2021', '07/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (17423, 212351000, 20826, 20826, 82845, 83244, '27/03/2021', '09/06/2021', '16/02/2021', '03/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (31374, 305176000, 10136, 29002, 78991, 79225, '22/03/2021', '24/05/2021', '02/03/2021', '24/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (19356, 249047000, 25007, 28082, 86476, 47409, '07/02/2021', '22/05/2021', '28/03/2021', '17/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (52877, 636092932, 14277, 29239, 54561, 9062, '31/03/2021', '13/05/2021', '17/02/2021', '20/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (98427, 636091400, 18867, 29749, 87507, 77328, '22/02/2021', '03/06/2021', '12/03/2021', '03/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (95119, 228339600, 15107, 29749, 67603, 723, '18/02/2021', '23/04/2021', '14/03/2021', '25/05/2021');
--end us407

--FOR US407
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (74072, 1, 303267000, 29239, 22522, 52328, 723, '24/01/2022', '25/01/2022', '24/01/2022', '25/01/2022');
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (65759, 2, 258692000, 21206, 18476, 79225, 77328, '25/01/2022', '26/01/2022', '25/01/2022', '26/01/2022');
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (55009, 3, 256888000, 18937, 10563, 9062, 25442, '26/01/2022', '27/01/2022', '26/01/2022', '27/01/2022');
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (14734, 4, 303221000, 25007, 30045, 66729, 34491, '27/01/2022', '28/01/2022', '27/01/2022', '28/01/2022');
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (47538, 5, 636091400, 17941, 20072, 83244, 93962, '28/01/2022', '29/01/2022', '28/01/2022', '29/01/2022');
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (97997, 6, 258692000, 21381, 21206, 77328, 86476, '29/01/2022', '30/01/2022', '29/01/2022', '30/01/2022');
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (7282, 1, 212351000, 21852, 18937, 54091, 78548, '30/01/2022', '01/02/2022', '30/01/2022', '01/02/2022');
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (9474, 2, 636091400, 20072, 18867, 53304, 69477, '24/01/2022', '25/01/2022', '24/01/2022', '25/01/2022');
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (79138, 3, 256888000, 17941, 21556, 53304, 89260, '25/01/2022', '26/01/2022', '25/01/2022', '26/01/2022');
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (69693, 4, 235092459, 29876, 24795, 54561, 77328, '26/01/2022', '27/01/2022', '26/01/2022', '27/01/2022');
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (29730, 5, 212180000, 13176, 13390, 73258, 54091, '27/01/2022', '28/01/2022', '27/01/2022', '28/01/2022');
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (6843, 6, 258692000, 14226, 224858, 9004, 93962, '28/01/2022', '29/01/2022', '28/01/2022', '29/01/2022');
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (68973, 1, 636019825, 20351, 20847, 96922, 37588, '29/01/2022', '30/01/2022', '29/01/2022', '30/01/2022');
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (18507, 2, 256888000, 18454, 13176, 69477, 73258, '30/01/2022', '31/01/2022', '30/01/2022', '31/01/2022');
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (69451, 3, 303221000, 29239, 10860, 86476, 57650, '31/01/2022', '01/02/2022', '31/01/2022', '01/02/2022');
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (75733, 4, 210950000, 20512, 18200, 66729, 78548, '01/02/2022', '02/02/2022', '01/02/2022', '02/02/2022');
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (26085, 5, 303267000, 21206, 21852, 45228, 79225, '02/02/2022', '03/02/2022', '02/02/2022', '03/02/2022');
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (17423, 6, 212351000, 20826, 20826, 82845, 83244, '03/02/2022', '04/02/2022', '03/02/2022', '04/02/2022');
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (31374, 1, 305176000, 10136, 29002, 78991, 79225, '04/02/2022', '05/02/2022', '04/02/2022', '05/02/2022');
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (19356, 2, 249047000, 25007, 28082, 86476, 47409, '05/02/2022', '06/02/2022', '05/02/2022', '06/02/2022');
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (52877, 3, 636092932, 14277, 29239, 54561, 9062, '06/02/2022', '07/02/2022', '06/02/2022', '07/02/2022');
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (98427, 4, 636091400, 18867, 29749, 87507, 77328, '07/02/2022', '08/02/2022', '07/02/2022', '08/02/2022');
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (95119, 5, 228339600, 15107, 29749, 67603, 723, '08/02/2022', '09/02/2022', '08/02/2022', '09/02/2022');
--END US407

insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (84683, 258692000, 21206, 21381, 723, 62698, '19/02/2021', '02/04/2021', '04/02/2021', '20/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (20458, 212180000, 27728, 24795, 82845, 723, '21/03/2021', '06/04/2021', '10/02/2021', '10/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (1792, 229767000, 16737, 24795, 3598, 80342, '22/02/2021', '28/04/2021', '01/03/2021', '12/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (56709, 258692000, 14113, 25350, 83244, 91736, '10/02/2021', '19/05/2021', '24/03/2021', '17/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (54452, 235092459, 28781, 21852, 47409, 59447, '02/03/2021', '02/04/2021', '02/02/2021', '15/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (95884, 636092932, 12543, 17941, 53304, 3598, '22/03/2021', '26/06/2021', '22/03/2021', '09/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (42101, 235092459, 12543, 28781, 62698, 64912, '19/02/2021', '13/06/2021', '29/03/2021', '26/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (23384, 228339600, 13012, 13390, 69477, 86476, '28/03/2021', '07/04/2021', '16/02/2021', '13/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (88084, 303221000, 28082, 13390, 79225, 53304, '26/03/2021', '11/04/2021', '25/02/2021', '27/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (98778, 258692000, 17386, 18867, 64229, 57546, '21/02/2021', '31/05/2021', '11/02/2021', '07/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (50660, 303221000, 17386, 20072, 2163, 77328, '09/02/2021', '04/04/2021', '17/03/2021', '21/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (12246, 305176000, 29973, 14459, 3598, 723, '03/02/2021', '27/05/2021', '27/02/2021', '22/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (30640, 305176000, 20072, 10358, 30009, 45806, '20/02/2021', '21/04/2021', '03/03/2021', '27/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (48427, 229961000, 18137, 14113, 64912, 53304, '11/03/2021', '13/05/2021', '04/03/2021', '20/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (16395, 235092459, 27792, 22522, 54561, 54091, '19/02/2021', '18/04/2021', '07/03/2021', '25/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (62883, 228339600, 20351, 10136, 45806, 82845, '22/02/2021', '24/06/2021', '02/03/2021', '20/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (95476, 212180000, 23247, 228264, 73258, 73258, '14/03/2021', '21/06/2021', '31/03/2021', '02/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (5322, 228339600, 21381, 21457, 87507, 45228, '23/03/2021', '09/06/2021', '28/03/2021', '25/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (15945, 229767000, 29239, 20351, 12366, 45806, '13/03/2021', '19/04/2021', '02/02/2021', '27/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (87776, 257881000, 29973, 21863, 78991, 2163, '25/02/2021', '12/04/2021', '19/03/2021', '03/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (27616, 258692000, 28082, 18454, 62590, 45228, '18/03/2021', '14/05/2021', '27/02/2021', '20/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (60579, 636019825, 14277, 246265, 83244, 78991, '06/03/2021', '14/06/2021', '20/02/2021', '24/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (95397, 256304000, 18200, 11180, 47409, 91736, '14/02/2021', '14/04/2021', '28/03/2021', '21/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (36230, 212351000, 10138, 29239, 16358, 19821, '02/03/2021', '21/05/2021', '11/02/2021', '19/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (43729, 256888000, 29749, 13176, 87507, 9062, '10/03/2021', '24/04/2021', '06/03/2021', '22/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (68399, 303221000, 28082, 14113, 82845, 78548, '25/03/2021', '10/04/2021', '13/02/2021', '25/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (41684, 212351000, 20847, 29749, 9062, 71971, '25/03/2021', '10/05/2021', '01/03/2021', '20/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (13590, 303221000, 16737, 21206, 89260, 53304, '28/02/2021', '25/04/2021', '28/03/2021', '17/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (27623, 212180000, 19473, 29239, 62590, 2025, '13/03/2021', '26/05/2021', '13/02/2021', '18/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (1159, 305176000, 10138, 11180, 89260, 89260, '29/03/2021', '11/06/2021', '25/03/2021', '27/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (92700, 229961000, 22226, 18200, 9062, 57546, '13/03/2021', '17/06/2021', '24/02/2021', '17/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (37671, 256888000, 29239, 10563, 73258, 71971, '23/03/2021', '01/05/2021', '27/02/2021', '11/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (41366, 636019825, 21556, 10138, 78548, 54561, '30/03/2021', '24/05/2021', '24/03/2021', '06/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (85163, 229767000, 10138, 21852, 3598, 39823, '10/02/2021', '07/05/2021', '10/02/2021', '16/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (26900, 258692000, 17386, 21852, 11031, 39823, '13/02/2021', '24/04/2021', '29/03/2021', '15/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (70578, 636019825, 20301, 27248, 12366, 723, '23/02/2021', '06/05/2021', '14/03/2021', '31/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (36207, 305373000, 10563, 24951, 71971, 54091, '13/03/2021', '30/04/2021', '01/03/2021', '24/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (46687, 258692000, 29239, 16485, 91736, 80342, '27/03/2021', '27/04/2021', '03/03/2021', '25/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (42398, 636091400, 246265, 26147, 34491, 73258, '21/03/2021', '17/04/2021', '07/02/2021', '04/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (9612, 303267000, 16485, 16737, 30009, 34893, '14/03/2021', '20/06/2021', '29/03/2021', '14/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (78430, 249047000, 27248, 25350, 80342, 54561, '15/02/2021', '05/06/2021', '07/03/2021', '23/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (37862, 636092932, 25350, 27728, 45228, 3598, '08/02/2021', '15/06/2021', '13/02/2021', '21/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (90619, 636091400, 20847, 10563, 57650, 30009, '09/02/2021', '18/06/2021', '28/02/2021', '08/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (13600, 303267000, 18137, 21451, 73258, 62698, '04/02/2021', '25/06/2021', '23/03/2021', '31/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (32347, 212351000, 18433, 18012, 80342, 37588, '08/03/2021', '15/05/2021', '29/03/2021', '23/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (76884, 305373000, 14470, 18433, 3598, 89260, '05/02/2021', '25/06/2021', '11/03/2021', '09/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (13689, 258692000, 21451, 228264, 67603, 66729, '03/02/2021', '28/04/2021', '07/03/2021', '24/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (20015, 257881000, 18137, 29876, 9062, 53304, '15/02/2021', '04/05/2021', '24/02/2021', '28/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (20945, 636019825, 28261, 29002, 77328, 71971, '26/02/2021', '21/05/2021', '04/03/2021', '16/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (33875, 305373000, 21863, 246265, 45806, 37588, '18/02/2021', '02/04/2021', '18/03/2021', '29/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (13993, 212180000, 11180, 28261, 66729, 45806, '14/02/2021', '22/06/2021', '18/02/2021', '03/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (73685, 258692000, 14470, 23428, 59447, 62698, '10/02/2021', '18/06/2021', '02/02/2021', '05/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (4622, 229961000, 15107, 14277, 93962, 54561, '02/03/2021', '16/05/2021', '16/02/2021', '25/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (5039, 235092459, 28261, 10136, 73258, 62590, '27/02/2021', '13/04/2021', '21/03/2021', '14/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (50905, 229767000, 25007, 22770, 85242, 25442, '01/02/2021', '08/05/2021', '13/03/2021', '03/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (89039, 249047000, 27248, 14470, 16358, 11031, '03/02/2021', '11/05/2021', '14/03/2021', '13/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (99629, 636091400, 21206, 23428, 52328, 64229, '02/02/2021', '22/05/2021', '04/03/2021', '30/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (48190, 212180000, 20351, 18326, 9062, 54561, '22/03/2021', '09/05/2021', '19/03/2021', '16/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (11922, 210950000, 20512, 28082, 34491, 54561, '08/02/2021', '12/05/2021', '02/03/2021', '24/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (18735, 229961000, 19473, 14635, 30009, 45228, '25/02/2021', '05/06/2021', '07/02/2021', '13/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (81775, 636092932, 20826, 22226, 87507, 52328, '01/03/2021', '13/06/2021', '25/02/2021', '02/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (81341, 212351000, 224858, 16485, 19821, 723, '06/02/2021', '18/06/2021', '26/02/2021', '08/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (53938, 305176000, 10136, 228264, 85242, 723, '08/02/2021', '28/06/2021', '02/02/2021', '02/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (58249, 305176000, 20301, 16737, 77328, 39823, '17/02/2021', '13/04/2021', '27/03/2021', '25/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (13910, 249047000, 23428, 28082, 37588, 83244, '17/02/2021', '29/05/2021', '06/02/2021', '15/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (81346, 249047000, 224858, 216592, 39823, 57546, '03/03/2021', '14/06/2021', '05/02/2021', '10/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (27123, 235092459, 13012, 20512, 39823, 11031, '22/03/2021', '17/06/2021', '31/03/2021', '12/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (15408, 303221000, 20826, 27248, 12366, 59447, '15/02/2021', '23/05/2021', '28/03/2021', '27/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (3248, 258692000, 21457, 12543, 78548, 86476, '19/03/2021', '20/05/2021', '19/02/2021', '24/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (29507, 636091400, 17386, 28261, 53304, 48193, '13/02/2021', '22/05/2021', '15/02/2021', '27/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (48103, 258692000, 21206, 21206, 54091, 45228, '07/02/2021', '30/06/2021', '26/02/2021', '10/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (2791, 636091400, 16737, 28781, 78991, 77328, '07/03/2021', '13/04/2021', '05/02/2021', '15/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (89828, 228339600, 224858, 18433, 34893, 80342, '17/03/2021', '09/05/2021', '27/02/2021', '06/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (57531, 305373000, 10860, 20512, 62590, 9062, '24/03/2021', '12/05/2021', '25/02/2021', '05/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (87639, 249047000, 14459, 11771, 45806, 57650, '16/02/2021', '13/04/2021', '23/02/2021', '30/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (34603, 636019825, 21381, 11174, 64229, 71971, '22/03/2021', '22/06/2021', '16/03/2021', '20/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (53004, 303221000, 23428, 24795, 48193, 64912, '06/03/2021', '20/04/2021', '08/02/2021', '05/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (84994, 249047000, 29876, 22522, 62590, 3598, '26/02/2021', '27/05/2021', '03/02/2021', '14/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (2863, 257881000, 19473, 11174, 2163, 16358, '15/02/2021', '26/05/2021', '09/03/2021', '19/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (87104, 305373000, 26147, 20512, 71971, 62698, '17/03/2021', '31/05/2021', '09/02/2021', '06/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (78351, 210950000, 29973, 26670, 73258, 45228, '21/03/2021', '29/04/2021', '03/02/2021', '16/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (97101, 212351000, 13012, 20847, 87507, 39823, '20/03/2021', '03/05/2021', '17/02/2021', '15/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (94394, 256888000, 27728, 29876, 16358, 82845, '19/03/2021', '11/06/2021', '25/03/2021', '11/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (81475, 258692000, 27728, 21206, 2025, 45228, '09/03/2021', '23/04/2021', '20/02/2021', '15/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (92725, 256888000, 26147, 17386, 2025, 89260, '06/02/2021', '22/04/2021', '27/02/2021', '14/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (14089, 636092932, 228264, 26147, 69477, 93962, '22/03/2021', '09/05/2021', '18/03/2021', '20/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (46943, 636091400, 14635, 20072, 86476, 25442, '24/02/2021', '16/04/2021', '28/02/2021', '25/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (56638, 229767000, 213737, 18867, 69477, 2025, '18/03/2021', '10/05/2021', '01/02/2021', '26/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (67131, 212351000, 13012, 11771, 39823, 45806, '11/02/2021', '30/06/2021', '11/03/2021', '17/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (86462, 258692000, 21852, 20512, 16358, 54091, '06/03/2021', '16/05/2021', '25/02/2021', '23/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (72026, 258692000, 20826, 20512, 12366, 54091, '16/02/2021', '15/06/2021', '05/03/2021', '12/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (37293, 212180000, 12543, 16485, 96922, 57650, '03/02/2021', '02/04/2021', '22/03/2021', '15/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (63980, 256888000, 18867, 18867, 77328, 62698, '11/02/2021', '29/05/2021', '22/02/2021', '06/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (46319, 229767000, 20847, 22226, 62590, 25442, '16/03/2021', '05/05/2021', '14/02/2021', '23/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (16455, 257881000, 29973, 19473, 12366, 82845, '25/02/2021', '15/05/2021', '14/03/2021', '06/06/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (62631, 303267000, 21206, 216592, 47409, 34893, '05/02/2021', '10/04/2021', '24/03/2021', '02/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (89077, 256888000, 14113, 20301, 19821, 16358, '21/03/2021', '06/04/2021', '02/02/2021', '10/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (74923, 258692000, 11771, 21852, 83244, 16358, '05/02/2021', '25/04/2021', '24/03/2021', '18/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (57602, 258692000, 17941, 10563, 62590, 54561, '18/02/2021', '09/06/2021', '04/03/2021', '12/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (46179, 258692000, 25350, 18200, 16358, 79225, '10/03/2021', '08/04/2021', '25/03/2021', '08/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (81259, 256888000, 22770, 10358, 34893, 73258, '13/03/2021', '19/05/2021', '13/03/2021', '29/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (81259, 256888000, 22770, 10358, 34893, 73258, '13/04/2021', '19/05/2021', '13/03/2021', '29/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (81239, 256888000, 22770, 10358, 34893, 73258, '23/04/2021', '19/05/2021', '13/03/2021', '01/04/2021');

insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (57601, 636092932, 17941, 10563, 62590, 54561, '16/05/2021', '09/06/2021', '04/03/2021', '12/05/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (57621, 636092932, 17941, 10563, 62590, 54561, '04/01/2022', '24/01/2022', null, null);
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (41171, 258692000, 25350, 18200, 16358, 79225, '10/03/2021', '08/04/2021', '25/03/2021', '08/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (46171, 258692000, 25350, 18200, 16358, 79225, '08/01/2022', '24/01/2022', null, null);
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (81251, 256888000, 22770, 10358, 34893, 73258, '13/03/2021', '19/05/2021', '13/03/2021', null);
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (80251, 256888000, 22770, 10358, 34893, 73258, '29/01/2022', '24/02/2022', null, null);
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (81254, 229767000, 22770, 10358, 34893, 73258, '13/04/2021', '19/05/2021', '13/03/2021', '29/04/2021');
insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (81232, 212180000, 22770, 10358, 34893, 73258, '23/04/2021', '19/05/2021', '13/03/2021', '01/04/2021');

--shipment
insert into shipment (container_id, shiptrip_id) values (9333060, 97101);
insert into shipment (container_id, shiptrip_id) values (2973602, 86462);
insert into shipment (container_id, shiptrip_id) values (6075721, 94394);
insert into shipment (container_id, shiptrip_id) values (8519913, 36929);
insert into shipment (container_id, shiptrip_id) values (8864057, 15408);
insert into shipment (container_id, shiptrip_id) values (6479280, 13631);
insert into shipment (container_id, shiptrip_id) values (3951071, 12246);
insert into shipment (container_id, shiptrip_id) values (5210890, 99629);
insert into shipment (container_id, shiptrip_id) values (6879109, 29507);
insert into shipment (container_id, shiptrip_id) values (5035824, 9474);
insert into shipment (container_id, shiptrip_id) values (1524166, 75733);
insert into shipment (container_id, shiptrip_id) values (9659312, 68626);
insert into shipment (container_id, shiptrip_id) values (4300882, 86152);
insert into shipment (container_id, shiptrip_id) values (1857493, 74590);
insert into shipment (container_id, shiptrip_id) values (4504662, 99784);
insert into shipment (container_id, shiptrip_id) values (4216799, 68399);
insert into shipment (container_id, shiptrip_id) values (6657019, 87104);
insert into shipment (container_id, shiptrip_id) values (5248256, 99784);
insert into shipment (container_id, shiptrip_id) values (7964863, 31374);
insert into shipment (container_id, shiptrip_id) values (9279781, 77575);
insert into shipment (container_id, shiptrip_id) values (9419579, 16395);
insert into shipment (container_id, shiptrip_id) values (7214991, 81341);
insert into shipment (container_id, shiptrip_id) values (2575956, 59732);
insert into shipment (container_id, shiptrip_id) values (2348350, 13689);
insert into shipment (container_id, shiptrip_id) values (8512993, 84994);
insert into shipment (container_id, shiptrip_id) values (9292064, 36929);
insert into shipment (container_id, shiptrip_id) values (9427410, 48427);
insert into shipment (container_id, shiptrip_id) values (7863279, 71434);
insert into shipment (container_id, shiptrip_id) values (1772644, 70007);
insert into shipment (container_id, shiptrip_id) values (682790, 15945);
insert into shipment (container_id, shiptrip_id) values (9554295, 87639);
insert into shipment (container_id, shiptrip_id) values (9883497, 14734);
insert into shipment (container_id, shiptrip_id) values (505155, 53526);
insert into shipment (container_id, shiptrip_id) values (209885, 47538);
insert into shipment (container_id, shiptrip_id) values (4853420, 92700);
insert into shipment (container_id, shiptrip_id) values (9868404, 54452);
insert into shipment (container_id, shiptrip_id) values (10933, 27623);
insert into shipment (container_id, shiptrip_id) values (9761539, 29730);
insert into shipment (container_id, shiptrip_id) values (2019607, 29507);
insert into shipment (container_id, shiptrip_id) values (2983468, 81259);
insert into shipment (container_id, shiptrip_id) values (7065594, 16455);
insert into shipment (container_id, shiptrip_id) values (9578352, 78351);
insert into shipment (container_id, shiptrip_id) values (3422706, 84683);
insert into shipment (container_id, shiptrip_id) values (4390453, 26900);
insert into shipment (container_id, shiptrip_id) values (3221270, 6843);
insert into shipment (container_id, shiptrip_id) values (8234084, 82933);
insert into shipment (container_id, shiptrip_id) values (7978977, 16347);
insert into shipment (container_id, shiptrip_id) values (307890, 3248);
insert into shipment (container_id, shiptrip_id) values (2061543, 98778);
insert into shipment (container_id, shiptrip_id) values (3904478, 56778);
insert into shipment (container_id, shiptrip_id) values (722415, 29440);
insert into shipment (container_id, shiptrip_id) values (1065235, 53004);
insert into shipment (container_id, shiptrip_id) values (1866154, 47538);
insert into shipment (container_id, shiptrip_id) values (708747, 9612);
insert into shipment (container_id, shiptrip_id) values (9666660, 5039);
insert into shipment (container_id, shiptrip_id) values (6122391, 92725);
insert into shipment (container_id, shiptrip_id) values (3908753, 29507);
insert into shipment (container_id, shiptrip_id) values (9326301, 29440);
insert into shipment (container_id, shiptrip_id) values (1354371, 78430);
insert into shipment (container_id, shiptrip_id) values (4953629, 5322);
insert into shipment (container_id, shiptrip_id) values (4871217, 29507);
insert into shipment (container_id, shiptrip_id) values (3705156, 55009);
insert into shipment (container_id, shiptrip_id) values (3761220, 1334);
insert into shipment (container_id, shiptrip_id) values (3633640, 34710);
insert into shipment (container_id, shiptrip_id) values (6049696, 99629);
insert into shipment (container_id, shiptrip_id) values (7977522, 98507);
insert into shipment (container_id, shiptrip_id) values (7800813, 56510);
insert into shipment (container_id, shiptrip_id) values (282200, 27623);
insert into shipment (container_id, shiptrip_id) values (6030852, 3133);
insert into shipment (container_id, shiptrip_id) values (4578305, 6843);
insert into shipment (container_id, shiptrip_id) values (3292360, 23255);
insert into shipment (container_id, shiptrip_id) values (7927862, 70578);
insert into shipment (container_id, shiptrip_id) values (2954426, 74590);
insert into shipment (container_id, shiptrip_id) values (4692520, 20458);
insert into shipment (container_id, shiptrip_id) values (4825735, 7282);
insert into shipment (container_id, shiptrip_id) values (2617744, 57602);
insert into shipment (container_id, shiptrip_id) values (8674855, 14734);
insert into shipment (container_id, shiptrip_id) values (7014398, 54675);
insert into shipment (container_id, shiptrip_id) values (1360235, 89077);
insert into shipment (container_id, shiptrip_id) values (4309735, 23183);
insert into shipment (container_id, shiptrip_id) values (4868136, 34710);
insert into shipment (container_id, shiptrip_id) values (4467720, 11922);
insert into shipment (container_id, shiptrip_id) values (4101616, 18735);
insert into shipment (container_id, shiptrip_id) values (7827555, 88427);
insert into shipment (container_id, shiptrip_id) values (9010621, 56510);
insert into shipment (container_id, shiptrip_id) values (9275571, 59448);
insert into shipment (container_id, shiptrip_id) values (2343659, 74923);
insert into shipment (container_id, shiptrip_id) values (1720027, 18507);
insert into shipment (container_id, shiptrip_id) values (8019448, 59732);
insert into shipment (container_id, shiptrip_id) values (9551907, 36230);
insert into shipment (container_id, shiptrip_id) values (7584299, 37293);
insert into shipment (container_id, shiptrip_id) values (6586081, 92725);
insert into shipment (container_id, shiptrip_id) values (4903273, 48103);
insert into shipment (container_id, shiptrip_id) values (1747858, 95954);
insert into shipment (container_id, shiptrip_id) values (8794102, 69482);
insert into shipment (container_id, shiptrip_id) values (8054992, 89964);
insert into shipment (container_id, shiptrip_id) values (598775, 87639);
insert into shipment (container_id, shiptrip_id) values (2501672, 13590);
insert into shipment (container_id, shiptrip_id) values (3785436, 27616);
insert into shipment (container_id, shiptrip_id) values (2059763, 20015);
insert into shipment (container_id, shiptrip_id) values (3848567, 88427);
insert into shipment (container_id, shiptrip_id) values (8561580, 89828);
insert into shipment (container_id, shiptrip_id) values (628900, 7326);
insert into shipment (container_id, shiptrip_id) values (140768, 30640);
insert into shipment (container_id, shiptrip_id) values (8288132, 89586);
insert into shipment (container_id, shiptrip_id) values (739179, 98427);
insert into shipment (container_id, shiptrip_id) values (2469210, 1792);
insert into shipment (container_id, shiptrip_id) values (7248906, 46943);
insert into shipment (container_id, shiptrip_id) values (3476951, 70578);
insert into shipment (container_id, shiptrip_id) values (4707570, 3248);
insert into shipment (container_id, shiptrip_id) values (2333562, 74072);
insert into shipment (container_id, shiptrip_id) values (3760110, 29045);
insert into shipment (container_id, shiptrip_id) values (915050, 63980);
insert into shipment (container_id, shiptrip_id) values (5465471, 27623);
insert into shipment (container_id, shiptrip_id) values (5510727, 2863);
insert into shipment (container_id, shiptrip_id) values (1053408, 68138);
insert into shipment (container_id, shiptrip_id) values (269705, 37671);
insert into shipment (container_id, shiptrip_id) values (7701223, 42398);
insert into shipment (container_id, shiptrip_id) values (2721157, 46179);
insert into shipment (container_id, shiptrip_id) values (2701262, 42648);
insert into shipment (container_id, shiptrip_id) values (2183409, 89077);
insert into shipment (container_id, shiptrip_id) values (8364967, 20015);
insert into shipment (container_id, shiptrip_id) values (9213039, 86152);
insert into shipment (container_id, shiptrip_id) values (4644478, 62631);
insert into shipment (container_id, shiptrip_id) values (7960424, 91586);
insert into shipment (container_id, shiptrip_id) values (110541, 62883);
insert into shipment (container_id, shiptrip_id) values (2449204, 56638);
insert into shipment (container_id, shiptrip_id) values (9518347, 6999);
insert into shipment (container_id, shiptrip_id) values (3213128, 52877);
insert into shipment (container_id, shiptrip_id) values (4687970, 59732);
insert into shipment (container_id, shiptrip_id) values (7684257, 6999);
insert into shipment (container_id, shiptrip_id) values (4166453, 60579);
insert into shipment (container_id, shiptrip_id) values (2814058, 53526);
insert into shipment (container_id, shiptrip_id) values (2693992, 63798);
insert into shipment (container_id, shiptrip_id) values (5734111, 41177);
insert into shipment (container_id, shiptrip_id) values (9070585, 34710);
insert into shipment (container_id, shiptrip_id) values (721797, 78430);
insert into shipment (container_id, shiptrip_id) values (7020916, 65759);
insert into shipment (container_id, shiptrip_id) values (1935697, 63980);
insert into shipment (container_id, shiptrip_id) values (5708813, 62883);
insert into shipment (container_id, shiptrip_id) values (8542533, 30640);
insert into shipment (container_id, shiptrip_id) values (1858468, 59324);
insert into shipment (container_id, shiptrip_id) values (8561707, 5322);
insert into shipment (container_id, shiptrip_id) values (2262730, 4622);
insert into shipment (container_id, shiptrip_id) values (7609909, 85922);
insert into shipment (container_id, shiptrip_id) values (3509579, 13590);
insert into shipment (container_id, shiptrip_id) values (7189955, 95954);
insert into shipment (container_id, shiptrip_id) values (5138381, 55237);
insert into shipment (container_id, shiptrip_id) values (7788724, 95119);
insert into shipment (container_id, shiptrip_id) values (2355235, 99629);
insert into shipment (container_id, shiptrip_id) values (9113177, 25803);
insert into shipment (container_id, shiptrip_id) values (9834739, 13398);
insert into shipment (container_id, shiptrip_id) values (3196942, 80020);
insert into shipment (container_id, shiptrip_id) values (8145745, 14089);
insert into shipment (container_id, shiptrip_id) values (2882607, 97997);
insert into shipment (container_id, shiptrip_id) values (8419671, 22209);
insert into shipment (container_id, shiptrip_id) values (6766832, 36929);
insert into shipment (container_id, shiptrip_id) values (7329290, 47433);
insert into shipment (container_id, shiptrip_id) values (4615675, 81346);
insert into shipment (container_id, shiptrip_id) values (4661451, 84901);
insert into shipment (container_id, shiptrip_id) values (6069919, 95397);
insert into shipment (container_id, shiptrip_id) values (4810054, 81259);
insert into shipment (container_id, shiptrip_id) values (5982865, 29960);
insert into shipment (container_id, shiptrip_id) values (9845504, 13993);
insert into shipment (container_id, shiptrip_id) values (5789700, 53004);
insert into shipment (container_id, shiptrip_id) values (6451050, 72026);
insert into shipment (container_id, shiptrip_id) values (9752298, 63980);
insert into shipment (container_id, shiptrip_id) values (9977728, 10710);
insert into shipment (container_id, shiptrip_id) values (5683858, 76884);
insert into shipment (container_id, shiptrip_id) values (2322868, 82933);
insert into shipment (container_id, shiptrip_id) values (122488, 35642);
insert into shipment (container_id, shiptrip_id) values (8928241, 12246);
insert into shipment (container_id, shiptrip_id) values (4774533, 48103);
insert into shipment (container_id, shiptrip_id) values (6589883, 80020);
insert into shipment (container_id, shiptrip_id) values (8059722, 46943);
insert into shipment (container_id, shiptrip_id) values (7778636, 47538);
insert into shipment (container_id, shiptrip_id) values (8763855, 84683);
insert into shipment (container_id, shiptrip_id) values (3587136, 60579);
insert into shipment (container_id, shiptrip_id) values (6409960, 14089);
insert into shipment (container_id, shiptrip_id) values (9874086, 64050);
insert into shipment (container_id, shiptrip_id) values (5019213, 23384);
insert into shipment (container_id, shiptrip_id) values (3760501, 75733);
insert into shipment (container_id, shiptrip_id) values (4142370, 4622);
insert into shipment (container_id, shiptrip_id) values (9280628, 99648);
insert into shipment (container_id, shiptrip_id) values (6558839, 42648);
insert into shipment (container_id, shiptrip_id) values (4993946, 81775);
insert into shipment (container_id, shiptrip_id) values (133065, 60579);
insert into shipment (container_id, shiptrip_id) values (3753096, 62631);
insert into shipment (container_id, shiptrip_id) values (790684, 41684);
insert into shipment (container_id, shiptrip_id) values (8574023, 68626);
insert into shipment (container_id, shiptrip_id) values (4162565, 18735);
insert into shipment (container_id, shiptrip_id) values (4924955, 97101);
insert into shipment (container_id, shiptrip_id) values (7840775, 84901);
insert into shipment (container_id, shiptrip_id) values (5577067, 56709);
insert into shipment (container_id, shiptrip_id) values (4411636, 41359);
insert into shipment (container_id, shiptrip_id) values (9496641, 46179);
insert into shipment (container_id, shiptrip_id) values (1123490, 20015);
insert into shipment (container_id, shiptrip_id) values (1738638, 62631);
insert into shipment (container_id, shiptrip_id) values (9892633, 46687);
insert into shipment (container_id, shiptrip_id) values (7514776, 95119);
insert into shipment (container_id, shiptrip_id) values (6107423, 95397);
insert into shipment (container_id, shiptrip_id) values (8039713, 33710);
insert into shipment (container_id, shiptrip_id) values (9001569, 69482);
insert into shipment (container_id, shiptrip_id) values (2096251, 47538);
insert into shipment (container_id, shiptrip_id) values (7489426, 54452);
insert into shipment (container_id, shiptrip_id) values (9946077, 59324);
insert into shipment (container_id, shiptrip_id) values (8356455, 23384);
insert into shipment (container_id, shiptrip_id) values (9605893, 73685);
insert into shipment (container_id, shiptrip_id) values (1555143, 15217);
insert into shipment (container_id, shiptrip_id) values (8475727, 53004);
insert into shipment (container_id, shiptrip_id) values (3492860, 15408);
insert into shipment (container_id, shiptrip_id) values (8501002, 53526);
insert into shipment (container_id, shiptrip_id) values (1736584, 98872);
insert into shipment (container_id, shiptrip_id) values (4299262, 23255);
insert into shipment (container_id, shiptrip_id) values (8591341, 23255);
insert into shipment (container_id, shiptrip_id) values (863716, 64368);
insert into shipment (container_id, shiptrip_id) values (5626896, 14089);
insert into shipment (container_id, shiptrip_id) values (2650387, 92700);
insert into shipment (container_id, shiptrip_id) values (9019057, 41359);
insert into shipment (container_id, shiptrip_id) values (2693114, 72026);
insert into shipment (container_id, shiptrip_id) values (7104943, 85163);
insert into shipment (container_id, shiptrip_id) values (441419, 34710);
insert into shipment (container_id, shiptrip_id) values (4783022, 46988);
insert into shipment (container_id, shiptrip_id) values (5579420, 13910);
insert into shipment (container_id, shiptrip_id) values (7078879, 68626);
insert into shipment (container_id, shiptrip_id) values (6807391, 47538);
insert into shipment (container_id, shiptrip_id) values (7874154, 26085);
insert into shipment (container_id, shiptrip_id) values (2609773, 16586);
insert into shipment (container_id, shiptrip_id) values (9741567, 16586);
insert into shipment (container_id, shiptrip_id) values (8546378, 23183);
insert into shipment (container_id, shiptrip_id) values (6397520, 16455);
insert into shipment (container_id, shiptrip_id) values (8914667, 57602);
insert into shipment (container_id, shiptrip_id) values (9143577, 48427);
insert into shipment (container_id, shiptrip_id) values (4326904, 88427);
insert into shipment (container_id, shiptrip_id) values (9137286, 25226);
insert into shipment (container_id, shiptrip_id) values (1305336, 31695);
insert into shipment (container_id, shiptrip_id) values (2136499, 89964);
insert into shipment (container_id, shiptrip_id) values (6198473, 95476);
insert into shipment (container_id, shiptrip_id) values (9764185, 13398);
insert into shipment (container_id, shiptrip_id) values (938340, 1334);
insert into shipment (container_id, shiptrip_id) values (896255, 88427);
insert into shipment (container_id, shiptrip_id) values (4404245, 54452);
insert into shipment (container_id, shiptrip_id) values (5947097, 68399);
insert into shipment (container_id, shiptrip_id) values (86822, 59732);
insert into shipment (container_id, shiptrip_id) values (3723819, 81346);
insert into shipment (container_id, shiptrip_id) values (1029352, 55237);
insert into shipment (container_id, shiptrip_id) values (4322877, 13600);
insert into shipment (container_id, shiptrip_id) values (5015254, 15217);
insert into shipment (container_id, shiptrip_id) values (7682515, 27123);
insert into shipment (container_id, shiptrip_id) values (5841368, 47538);
insert into shipment (container_id, shiptrip_id) values (2733809, 81259);
insert into shipment (container_id, shiptrip_id) values (8056115, 83011);
insert into shipment (container_id, shiptrip_id) values (7560668, 95476);
insert into shipment (container_id, shiptrip_id) values (7082372, 2863);
insert into shipment (container_id, shiptrip_id) values (5655299, 68973);
insert into shipment (container_id, shiptrip_id) values (5308377, 30076);
insert into shipment (container_id, shiptrip_id) values (9216307, 74072);
insert into shipment (container_id, shiptrip_id) values (2031503, 74590);
insert into shipment (container_id, shiptrip_id) values (9978599, 25226);
insert into shipment (container_id, shiptrip_id) values (978199, 84901);
insert into shipment (container_id, shiptrip_id) values (4481941, 29960);
insert into shipment (container_id, shiptrip_id) values (2672406, 48103);
insert into shipment (container_id, shiptrip_id) values (1987586, 16586);
insert into shipment (container_id, shiptrip_id) values (7807239, 1792);
insert into shipment (container_id, shiptrip_id) values (144449, 84901);
insert into shipment (container_id, shiptrip_id) values (9795786, 76372);
insert into shipment (container_id, shiptrip_id) values (5312279, 99629);
insert into shipment (container_id, shiptrip_id) values (7749375, 82933);
insert into shipment (container_id, shiptrip_id) values (2504454, 98427);
insert into shipment (container_id, shiptrip_id) values (1558071, 55009);
insert into shipment (container_id, shiptrip_id) values (1962396, 2863);
insert into shipment (container_id, shiptrip_id) values (2353883, 84994);
insert into shipment (container_id, shiptrip_id) values (3434488, 11922);
insert into shipment (container_id, shiptrip_id) values (1418596, 76372);
insert into shipment (container_id, shiptrip_id) values (6956397, 68138);
insert into shipment (container_id, shiptrip_id) values (123768, 25226);
insert into shipment (container_id, shiptrip_id) values (2722759, 92700);
insert into shipment (container_id, shiptrip_id) values (6458130, 94394);
insert into shipment (container_id, shiptrip_id) values (9608504, 92725);
insert into shipment (container_id, shiptrip_id) values (3462818, 41177);
insert into shipment (container_id, shiptrip_id) values (6724197, 13590);
insert into shipment (container_id, shiptrip_id) values (5659368, 80020);
insert into shipment (container_id, shiptrip_id) values (1274563, 68399);
insert into shipment (container_id, shiptrip_id) values (837583, 62258);
insert into shipment (container_id, shiptrip_id) values (586095, 68138);
insert into shipment (container_id, shiptrip_id) values (9661114, 72026);
insert into shipment (container_id, shiptrip_id) values (7572031, 55237);
insert into shipment (container_id, shiptrip_id) values (8218495, 97997);
insert into shipment (container_id, shiptrip_id) values (2767540, 86152);
insert into shipment (container_id, shiptrip_id) values (745998, 53004);
insert into shipment (container_id, shiptrip_id) values (3795095, 58249);
insert into shipment (container_id, shiptrip_id) values (1986974, 16395);
insert into shipment (container_id, shiptrip_id) values (6889907, 70578);
insert into shipment (container_id, shiptrip_id) values (5638346, 10710);
insert into shipment (container_id, shiptrip_id) values (540114, 81259);
insert into shipment (container_id, shiptrip_id) values (8935466, 37671);
insert into shipment (container_id, shiptrip_id) values (3576467, 6843);
insert into shipment (container_id, shiptrip_id) values (8498533, 80020);
insert into shipment (container_id, shiptrip_id) values (6278703, 29045);
insert into shipment (container_id, shiptrip_id) values (5985041, 64050);
insert into shipment (container_id, shiptrip_id) values (3000853, 79138);
insert into shipment (container_id, shiptrip_id) values (8950208, 68973);
insert into shipment (container_id, shiptrip_id) values (9987446, 89077);
insert into shipment (container_id, shiptrip_id) values (810979, 89586);
insert into shipment (container_id, shiptrip_id) values (9226749, 1159);
insert into shipment (container_id, shiptrip_id) values (3989030, 42101);
insert into shipment (container_id, shiptrip_id) values (9203377, 78351);
insert into shipment (container_id, shiptrip_id) values (283730, 14734);
insert into shipment (container_id, shiptrip_id) values (4074022, 56510);
insert into shipment (container_id, shiptrip_id) values (7459573, 65759);
insert into shipment (container_id, shiptrip_id) values (9466123, 20015);
insert into shipment (container_id, shiptrip_id) values (5540054, 71434);
insert into shipment (container_id, shiptrip_id) values (7857665, 57052);
insert into shipment (container_id, shiptrip_id) values (1277238, 56510);
insert into shipment (container_id, shiptrip_id) values (2750735, 84994);
insert into shipment (container_id, shiptrip_id) values (5359558, 64368);
insert into shipment (container_id, shiptrip_id) values (6915770, 29730);
insert into shipment (container_id, shiptrip_id) values (5351520, 19356);
insert into shipment (container_id, shiptrip_id) values (6680360, 88084);
insert into shipment (container_id, shiptrip_id) values (3401835, 5322);
insert into shipment (container_id, shiptrip_id) values (6064923, 76372);
insert into shipment (container_id, shiptrip_id) values (4220768, 83011);
insert into shipment (container_id, shiptrip_id) values (8529220, 9474);
insert into shipment (container_id, shiptrip_id) values (6215351, 37671);
insert into shipment (container_id, shiptrip_id) values (5919556, 50905);
insert into shipment (container_id, shiptrip_id) values (857965, 18735);
insert into shipment (container_id, shiptrip_id) values (7226444, 89077);
insert into shipment (container_id, shiptrip_id) values (7852564, 86462);
insert into shipment (container_id, shiptrip_id) values (9917166, 22209);
insert into shipment (container_id, shiptrip_id) values (7850304, 26085);
insert into shipment (container_id, shiptrip_id) values (1573723, 75733);
insert into shipment (container_id, shiptrip_id) values (7511557, 13600);
insert into shipment (container_id, shiptrip_id) values (1623809, 95884);
insert into shipment (container_id, shiptrip_id) values (3652803, 16347);
insert into shipment (container_id, shiptrip_id) values (2818829, 68626);
insert into shipment (container_id, shiptrip_id) values (4897238, 33875);
insert into shipment (container_id, shiptrip_id) values (8255706, 94394);
insert into shipment (container_id, shiptrip_id) values (8889089, 56778);
insert into shipment (container_id, shiptrip_id) values (5967879, 99648);
insert into shipment (container_id, shiptrip_id) values (3921493, 48547);
insert into shipment (container_id, shiptrip_id) values (5179239, 92700);
insert into shipment (container_id, shiptrip_id) values (6441995, 55009);
insert into shipment (container_id, shiptrip_id) values (551621, 75733);
insert into shipment (container_id, shiptrip_id) values (8965558, 6999);
insert into shipment (container_id, shiptrip_id) values (8213390, 47433);
insert into shipment (container_id, shiptrip_id) values (7515848, 47538);
insert into shipment (container_id, shiptrip_id) values (7136029, 54452);
insert into shipment (container_id, shiptrip_id) values (961837, 47538);
insert into shipment (container_id, shiptrip_id) values (5063635, 22209);
insert into shipment (container_id, shiptrip_id) values (2753171, 13398);
insert into shipment (container_id, shiptrip_id) values (3913156, 13993);
insert into shipment (container_id, shiptrip_id) values (6107524, 84683);
insert into shipment (container_id, shiptrip_id) values (7525736, 5230);
insert into shipment (container_id, shiptrip_id) values (6626959, 18507);
insert into shipment (container_id, shiptrip_id) values (1152784, 85922);
insert into shipment (container_id, shiptrip_id) values (2755128, 1792);
insert into shipment (container_id, shiptrip_id) values (3513855, 70578);
insert into shipment (container_id, shiptrip_id) values (9469931, 14089);
insert into shipment (container_id, shiptrip_id) values (4515914, 48547);
insert into shipment (container_id, shiptrip_id) values (7715183, 3133);
insert into shipment (container_id, shiptrip_id) values (309787, 47538);
insert into shipment (container_id, shiptrip_id) values (2913804, 29507);
insert into shipment (container_id, shiptrip_id) values (4399451, 64050);
insert into shipment (container_id, shiptrip_id) values (386410, 64368);
insert into shipment (container_id, shiptrip_id) values (8539085, 76372);
insert into shipment (container_id, shiptrip_id) values (9355505, 4622);
insert into shipment (container_id, shiptrip_id) values (9000932, 89828);
insert into shipment (container_id, shiptrip_id) values (2116393, 81341);
insert into shipment (container_id, shiptrip_id) values (2549246, 16347);
insert into shipment (container_id, shiptrip_id) values (6191019, 98427);
insert into shipment (container_id, shiptrip_id) values (6955932, 81259);
insert into shipment (container_id, shiptrip_id) values (3272382, 58249);
insert into shipment (container_id, shiptrip_id) values (1475321, 74590);
insert into shipment (container_id, shiptrip_id) values (666915, 6843);
insert into shipment (container_id, shiptrip_id) values (9803333, 48427);
insert into shipment (container_id, shiptrip_id) values (3011850, 98872);
insert into shipment (container_id, shiptrip_id) values (1506075, 53938);
insert into shipment (container_id, shiptrip_id) values (5887416, 23255);
insert into shipment (container_id, shiptrip_id) values (7636252, 81475);
insert into shipment (container_id, shiptrip_id) values (8527619, 16586);
insert into shipment (container_id, shiptrip_id) values (3355821, 62258);
insert into shipment (container_id, shiptrip_id) values (5002135, 57052);
insert into shipment (container_id, shiptrip_id) values (3143910, 41366);
insert into shipment (container_id, shiptrip_id) values (7305599, 99648);
insert into shipment (container_id, shiptrip_id) values (6686080, 27623);
insert into shipment (container_id, shiptrip_id) values (2661497, 89586);
insert into shipment (container_id, shiptrip_id) values (2877511, 92700);
insert into shipment (container_id, shiptrip_id) values (4847076, 92700);
insert into shipment (container_id, shiptrip_id) values (3113560, 95397);
insert into shipment (container_id, shiptrip_id) values (7213722, 47433);
insert into shipment (container_id, shiptrip_id) values (1049486, 88084);
insert into shipment (container_id, shiptrip_id) values (4889108, 94394);
insert into shipment (container_id, shiptrip_id) values (8284377, 98507);
insert into shipment (container_id, shiptrip_id) values (1382188, 81341);
insert into shipment (container_id, shiptrip_id) values (2802505, 35642);
insert into shipment (container_id, shiptrip_id) values (9803333, 48427);


--FUNÇÃO CORRETA
create or replace function get_route_id(f_container_id container.container_id%type, f_route_id integer) return varchar
is
    container_exists integer;
    ex_invalid_container_id exception;
    ex_not_leased_client exception;
begin


    select count(*) into container_exists from container where container_id=f_container_id;

    if container_exists = 0 then
        raise ex_invalid_container_id;
    else
        return f_route_id;
    end if;


exception
    when ex_invalid_container_id then
        return '10 – invalid container id';
end;

SET SERVEROUTPUT ON;
begin
    dbms_output.put_line('The location id is: '||get_route_id(1,33));
end;