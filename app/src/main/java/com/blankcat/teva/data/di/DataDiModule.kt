package com.blankcat.teva.data.di

import android.content.Context
import androidx.room.Room
import com.blankcat.teva.data.dao.AppDao
import com.blankcat.teva.data.dao.CardDao
import com.blankcat.teva.data.dao.CartNoteDao
import com.blankcat.teva.data.dao.CouponDao
import com.blankcat.teva.data.dao.ProductDao
import com.blankcat.teva.data.dao.TransactionRunner
import com.blankcat.teva.data.dao.UserDao
import com.blankcat.teva.data.db.TevaDatabase
import com.blankcat.teva.repository.AppRepository
import com.blankcat.teva.repository.AuthRepository
import com.blankcat.teva.repository.CardRepository
import com.blankcat.teva.repository.CartNoteRepository
import com.blankcat.teva.repository.CouponRepository
import com.blankcat.teva.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataDiModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TevaDatabase {
        return Room.databaseBuilder(
            context,
            TevaDatabase::class.java,
            "data.db",
        ).build()
    }


    @Provides
    @Singleton
    fun provideUserDao(database: TevaDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(userDao: UserDao): AuthRepository = AuthRepository(userDao)

    @Provides
    @Singleton
    fun provideTransactionRunner(database: TevaDatabase): TransactionRunner =
        database.transactionRunnerDao()

    @Provides
    @Singleton
    fun provideAppDao(database: TevaDatabase): AppDao = database.appDao()

    @Provides
    @Singleton
    fun provideAppRepository(appDao: AppDao): AppRepository = AppRepository(appDao)


    @Provides
    @Singleton
    fun provideProductsDao(database: TevaDatabase): ProductDao = database.productDao()

    @Provides
    @Singleton
    fun provideProductRepository(productDao: ProductDao): ProductRepository =
        ProductRepository(productDao)


    @Provides
    @Singleton
    fun provideCartNoteDao(database: TevaDatabase): CartNoteDao = database.cartNoteDao()

    @Provides
    @Singleton
    fun provideCartNoteRepository(cartNoteDao: CartNoteDao): CartNoteRepository =
        CartNoteRepository(cartNoteDao)


    @Provides
    @Singleton
    fun provideCardDao(database: TevaDatabase): CardDao = database.cardDao()

    @Provides
    @Singleton
    fun provideCardRepository(cardDao: CardDao): CardRepository = CardRepository(cardDao)

    @Provides
    @Singleton
    fun provideCouponDao(database: TevaDatabase): CouponDao = database.couponDao()

    @Provides
    @Singleton
    fun provideCouponRepository(couponDao: CouponDao): CouponRepository =
        CouponRepository(couponDao)
}