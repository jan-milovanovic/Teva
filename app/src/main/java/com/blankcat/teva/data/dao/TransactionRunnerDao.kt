package com.blankcat.teva.data.dao

import androidx.room.Dao
import androidx.room.Ignore
import androidx.room.Transaction

interface TransactionRunner {
    suspend operator fun invoke(tx: suspend () -> Unit)
}

@Dao
abstract class TransactionRunnerDao: TransactionRunner  {
    @Transaction
    protected open suspend fun runInTransaction(tx: suspend () -> Unit) = tx()

    @Ignore
    override suspend fun invoke(tx: suspend () -> Unit) {
        runInTransaction(tx)
    }
}