package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 予約情報を表すクラス
 */
public class Reservation {

    private int id;
    private int productId;
    private int customerId;
    private LocalDate reservationDate;
    private LocalDate returnPlanDate;
    private LocalDateTime returnDateTime; // 未返却の場合はnull

    public Reservation(int id, int productId, int customerId,
            LocalDate reservationDate, LocalDate returnPlanDate) {
        this.id = id;
        this.productId = productId;
        this.customerId = customerId;
        this.reservationDate = reservationDate;
        this.returnPlanDate = returnPlanDate;
        this.returnDateTime = null;
    }

    public int getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public LocalDate getReturnPlanDate() {
        return returnPlanDate;
    }

    public void setReturnPlanDate(LocalDate returnPlanDate) {
        this.returnPlanDate = returnPlanDate;
    }

    public LocalDateTime getReturnDateTime() {
        return returnDateTime;
    }

    public void setReturnDateTime(LocalDateTime returnDateTime) {
        this.returnDateTime = returnDateTime;
    }

    /**
     * まだ返却されていない予約かどうかを判定する
     */
    public boolean isActive() {
        return returnDateTime == null;
    }

    @Override
    public String toString() {
        String returnInfo = (returnDateTime == null) ? "未返却" : "返却済み(" + returnDateTime + ")";
        return "予約ID:" + id + " 物品ID:" + productId + " 顧客ID:" + customerId
                + " 予約日:" + reservationDate + " 返却予定日:" + returnPlanDate
                + " 状態:" + returnInfo;
    }
}
