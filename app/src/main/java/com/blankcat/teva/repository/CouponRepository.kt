package com.blankcat.teva.repository

import com.blankcat.teva.data.dao.CouponDao
import com.blankcat.teva.models.TevaBarcode
import kotlinx.coroutines.flow.Flow

class CouponRepository(private val couponDao: CouponDao) {
    fun getCoupons(): Flow<List<TevaBarcode>> = couponDao.getCoupons()

    fun deleteCoupons(coupon: TevaBarcode) = couponDao.deleteCoupon(coupon)

    fun insertCoupon(coupon: TevaBarcode) = couponDao.insertCoupon(coupon)

    fun updateCoupon(coupon: TevaBarcode) = couponDao.updateCoupon(coupon)
}