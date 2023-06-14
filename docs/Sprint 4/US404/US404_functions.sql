--function to check if the ship has been idle on a given day
CREATE OR REPLACE FUNCTION check_ship_idle_day(f_ship_mmsi ship.mmsi%type, f_day_date date) return boolean
IS
   c_real_departure_date date;
   c_real_arrival_date date;
   c_shiptrip_id shiptrip.shiptrip_id%type;

    CURSOR c_trips is
      SELECT shiptrip_id, real_departure_date, real_arrival_date FROM SHIPTRIP WHERE MMSI=f_ship_mmsi;

BEGIN
    OPEN c_trips;
    LOOP
    FETCH c_trips into c_shiptrip_id, c_real_departure_date, c_real_arrival_date;
        EXIT WHEN c_trips%notfound;
        --if the ship had at least a shiptrip where it has left OR arrived in that date OR the date was in the middle of the trip in that date, it means the ship was not idle in that day
        IF (f_day_date >= c_real_departure_date AND f_day_date <= c_real_arrival_date) THEN
            return false;
        END IF;

    END LOOP;
    CLOSE c_trips;

    return true;

    exception
        when no_data_found then
            return false;
END;
/

--function to count the days a ship has been idle since the beginning of the year
CREATE OR REPLACE FUNCTION check_ship_idle_year(f_ship_mmsi ship.mmsi%type, f_start_year date) return integer
IS
    count_days integer := 0;
    current_date date := f_start_year;
    isIdleOnDay boolean;

BEGIN
    WHILE to_char(current_date, 'DD-MM-YYYY') != to_char(sysdate+1, 'DD-MM-YYYY')
    LOOP
        isIdleOnDay := check_ship_idle_day(f_ship_mmsi, current_date);
        IF isIdleOnDay THEN
            count_days := count_days+1;
        END IF;
        current_date := current_date+1;
    END LOOP;

    return count_days;

    exception
        when no_data_found then
            return null;
END;
/

--function to get for each ship the idle days
CREATE OR REPLACE FUNCTION all_ships_idle(f_start_year varchar) return varchar
IS
    c_mmsi  ship.mmsi%type;
    ships varchar(32700);
    idle_days integer := 0;
    start_year date := TO_DATE(f_start_year, 'DD-MM-YYYY');

    CURSOR c_ships is
      SELECT mmsi FROM SHIP;
BEGIN
    OPEN c_ships;
    LOOP
    FETCH c_ships into c_mmsi;
      EXIT WHEN c_ships%notfound;
    idle_days := check_ship_idle_year(c_mmsi, start_year);
    ships := ships || 'Ship MMSI: ' || c_mmsi || ' | Number of Idle Days: ' || idle_days || chr(10);
    END LOOP;
    CLOSE c_ships;

    return ships;

    exception
        when no_data_found then
            return ('No data found');
END;
/

--ATTENTION: this tests were made when sysdate='14-01-2022' therefore the result will change in the future

--TESTS FOR check_ship_idle_day

SET SERVEROUTPUT ON;
--ship with mmsi 228339600 has a shiptrip with real departure and arrival date = 01-01-2022, therefore FALSE (not idle)
declare
    v1 boolean;
begin
    v1 := check_ship_idle_day(228339600, '01-01-2022');
    dbms_output.put_line('result ' || case when v1 then 'true' else 'false' end);
end;

--ship with mmsi 228339600 has a shiptrip with real departure date = 01-01-2022, therefore FALSE (not idle)
declare
    v1 boolean;
begin
    v1 := check_ship_idle_day(228339600, '06-01-2022');
    dbms_output.put_line('result ' || case when v1 then 'true' else 'false' end);
end;

--ship with mmsi 228339600 has a shiptrip with real arrival date = 07-01-2022, therefore FALSE (not idle)
declare
    v1 boolean;
begin
    v1 := check_ship_idle_day(228339600, '07-01-2022');
    dbms_output.put_line('result ' || case when v1 then 'true' else 'false' end);
end;

--ship with mmsi 228339600 has no shiptrip where its real arrival date or real departure date = 05-01-2022 NOR a shiptrip where it's still occuring in that date, therefore TRUE (is idle)
declare
    v1 boolean;
begin
    v1 := check_ship_idle_day(228339600, '05-01-2022');
    dbms_output.put_line('result ' || case when v1 then 'true' else 'false' end);
end;




--TESTS FOR check_ship_idle_year

--ship with mmsi 228339600 has been idle in date 05-01-2022, 08-01-2022 and 13-01-2022 since beginning of current year
--therefore returns 3
begin
    dbms_output.put_line(check_ship_idle_year(228339600, '01-01-2022'));
end;


--ship with mmsi 228339600 has been idle in date 01-01-2022, 02-01-2022, 06-01-2022, 09-01-2022, 13-01-2022, 14-01-2022 since beginning of current year
--therefore returns 6
begin
    dbms_output.put_line(check_ship_idle_year(229767000, '01-01-2022'));
end;

--ship with mmsi 258692000 has been idle in date 01-01-2022, 07-01-2022, 08-01-2022, 12-01-2022, 14-01-2022 since beginning of current year
--therefore returns 5
begin
    dbms_output.put_line(check_ship_idle_year(258692000, '01-01-2022'));
end;

--ship with mmsi 636019825 has been idle in date 01-01-2022, 02-01-2022, 03-01-2022, 04-01-2022, 07-01-2022, 08-01-2022, 09-01-2022 since beginning of current year
--therefore returns 7
begin
    dbms_output.put_line(check_ship_idle_year(636019825, '01-01-2022'));
end;

--ship with mmsi 210950000 has no ship trips for the current year yet so it has been always idle
--therefore returns number of days since the beginning of the year until the current date
begin
    dbms_output.put_line(check_ship_idle_year(210950000, '01-01-2022'));
end;

begin
    dbms_output.put_line(all_ships_idle('01-01-2022'));
end;



