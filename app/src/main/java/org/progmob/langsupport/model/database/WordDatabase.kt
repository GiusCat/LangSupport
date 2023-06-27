package org.progmob.langsupport.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.progmob.langsupport.model.WordData
import org.progmob.langsupport.util.Converters

@Database(entities = [WordData::class], version = 1)
@TypeConverters(Converters::class)
abstract class WordDatabase: RoomDatabase() {
    abstract fun wordDao(): WordDao
}