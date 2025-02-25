package com.shakilpatel.bytebuddy.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.shakilpatel.bytebuddy.data.repo.AuthRepo
import com.shakilpatel.bytebuddy.data.repo.CommonRepo
import com.shakilpatel.bytebuddy.data.repo.ErrorRepo
import com.shakilpatel.bytebuddy.data.repo.NotesRepo
import com.shakilpatel.bytebuddy.data.repo.ProfileRepo
import com.shakilpatel.bytebuddy.data.repo.ProjectRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideFirestoreDB() = FirebaseFirestore.getInstance()

    @Provides
    fun provideRealtimeDB() = FirebaseDatabase.getInstance()

    @Provides
    fun provideStorage() = FirebaseStorage.getInstance()

    @Provides
    fun provideAuth() = FirebaseAuth.getInstance()

    @Provides
    fun provideAuthRepo(db: FirebaseFirestore, auth: FirebaseAuth, context: Context): AuthRepo =
        AuthRepo(
            db, auth, context
        )

    @Provides
    fun provideCommonRepo(db: FirebaseFirestore, database: FirebaseDatabase,auth: FirebaseAuth): CommonRepo =
        CommonRepo(db, database,auth)

    @Provides
    fun provideErrorRepo(db: FirebaseFirestore, auth: FirebaseAuth,commonRepo: CommonRepo): ErrorRepo = ErrorRepo(db, auth,commonRepo)

    @Provides
    fun provideNotesRepo(
        auth: FirebaseAuth,
        db: FirebaseFirestore,
        storage: FirebaseStorage,
        database: FirebaseDatabase,
        commonRepo: CommonRepo
    ): NotesRepo = NotesRepo(auth, db, storage, database,commonRepo)

    @Provides
    fun provideProfileRepo(db: FirebaseFirestore): ProfileRepo = ProfileRepo(db)

    @Provides
    fun provideProjectRepo(db: FirebaseFirestore): ProjectRepo = ProjectRepo(db)

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context
}