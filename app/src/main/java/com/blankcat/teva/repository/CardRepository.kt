package com.blankcat.teva.repository

import com.blankcat.teva.data.dao.CardDao
import com.blankcat.teva.models.TevaBarcode
import kotlinx.coroutines.flow.Flow

class CardRepository(private val cardDao: CardDao) {
    fun getCards(): Flow<List<TevaBarcode>> = cardDao.getCards()

    fun deleteCards(tevaBarcode: TevaBarcode) = cardDao.deleteCard(tevaBarcode)

    fun insertCard(tevaBarcode: TevaBarcode) = cardDao.insertCard(tevaBarcode)

    fun updateCard(tevaBarcode: TevaBarcode) = cardDao.updateCard(tevaBarcode)
}