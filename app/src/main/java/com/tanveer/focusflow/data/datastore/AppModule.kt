package com.tanveer.focusflow.data.datastore

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.tanveer.focusflow.data.firestore.SessionRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.tanveer.focusflow.data.firestore.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
    @Provides
    @Singleton
    fun provideSessionRepository(
        firestore: FirebaseFirestore
    ): SessionRepository = SessionRepository(firestore)

    @Provides
    @Singleton
    fun provideSettingDataStore(
        @ApplicationContext context: Context
    ): SettingDataStore = SettingDataStore(context)

    @Provides
    @Singleton
    fun provideTaskRepository(db: FirebaseFirestore): TaskRepository = TaskRepository(db)
}
