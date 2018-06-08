package com.sefirah.myomer.realm;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by ahmad on 4/7/18.
 */

public class DatabaseMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        RealmSchema schema = realm.getSchema();

        if (oldVersion == 0) {
            schema.get("JournalQuestionModel")
                    .removePrimaryKey()
                    .addField("uniqueId", long.class, FieldAttribute.PRIMARY_KEY);
            oldVersion++;
        }

    }
}
