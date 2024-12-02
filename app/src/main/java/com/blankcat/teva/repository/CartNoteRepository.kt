package com.blankcat.teva.repository

import com.blankcat.teva.data.dao.CartNoteDao
import com.blankcat.teva.models.CartNote
import kotlinx.coroutines.flow.Flow

class CartNoteRepository(private val cartNoteDao: CartNoteDao) {
    fun getNote(): Flow<CartNote?> = cartNoteDao.getNote()

    fun insertNote(cartNote: CartNote) = cartNoteDao.insertNote(cartNote)
}