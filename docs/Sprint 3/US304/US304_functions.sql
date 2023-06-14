--

create or replace trigger TRG_AUDIT_CONTAINER_IN_MANIFEST
after insert or update or delete on CONTAINERINCARGOMANIFEST
for each row
declare
    l_operation varchar(6) :=
        case when updating then 'UPDATE'
            when deleting then 'DELETE'
            else 'INSERT' end;
    begin
        if updating or inserting then
            insert into AuditContainerInManifest
                (aud_who,
                aud_when,
                aud_operation,
                container_id,
                cargoManifest_id,
                temperature_kept
                )
                values(
                    user,
                    sysdate,
                    l_operation,
                    :new.container_id,
                    :new.cargoManifest_id,
                    :new.temperature_kept
                );
            else
                insert into AuditContainerInManifest
                (aud_who,
                aud_when,
                aud_operation,
                container_id,
                cargoManifest_id,
                temperature_kept
                ) values(
                    user,
                    sysdate,
                    l_operation,
                    :old.container_id,
                    :old.cargoManifest_id,
                    :old.temperature_kept
                    );
            end if;
        end;