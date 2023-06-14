--US207--

CREATE OR REPLACE PROCEDURE count_CargoManifests_Avg_Containers (givenYear in Varchar, mmsiCode in Varchar, numCargoManifests out Integer, mediaCont out Integer)
IS
    shipscode Ship.mmsi%type;
    loadcargoscode cargomanifest.cargomanifest_id%type;
    unloadcargoscode cargomanifest.cargomanifest_id%type;
    numContainersInCm Integer;
    somaTotalContainer Integer;
    contadorCm Integer;

    Cursor cargosLoad IS
        Select loading_cargo_id
        from shiptrip
        where mmsi=mmsiCode AND extract(year from real_departure_date)=givenYear;

    Cursor cargosUnload IS
        Select unloading_cargo_id
        from shiptrip
        where mmsi=mmsiCode AND extract(year from real_arrival_date)=givenYear;
BEGIN
    contadorCm :=0;
    somaTotalContainer:=0;

   open cargosLoad;
    LOOP
        fetch cargosLoad INTO loadcargoscode;
        Exit When cargosLoad%notfound;
        dbms_output.put_line('cargo load id: ' || loadcargoscode);
        contadorCm := contadorCm + 1;
        dbms_output.put_line('cargocount ' || contadorCm);
        select count(*) into numContainersInCm from containerincargomanifest where cargomanifest_id=loadcargoscode;
        somaTotalContainer := somaTotalContainer + numContainersInCm;
        dbms_output.put_line('cont count ' || somaTotalContainer);
    END LOOP;

    open cargosUnload;
     LOOP
        fetch cargosUnload INTO unloadcargoscode;
        Exit When cargosUnload%notfound;
        dbms_output.put_line('cargo unload id: ' || unloadcargoscode);
        contadorCm := contadorCm + 1;
        dbms_output.put_line('cargocount ' || contadorCm);
        select count(*) into numContainersInCm from containerincargomanifest where cargomanifest_id=unloadcargoscode;
        somaTotalContainer := somaTotalContainer + numContainersInCm;
        dbms_output.put_line('cont count ' || somaTotalContainer);
    END LOOP;

    numCargoManifests := contadorCm;
    mediaCont := (somaTotalContainer / contadorCm);
    dbms_output.put_line('final number of CM ' || numCargoManifests);
    dbms_output.put_line('mean of containers ' || mediaCont);
END;