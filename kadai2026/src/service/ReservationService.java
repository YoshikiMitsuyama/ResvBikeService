package service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import model.Reservation;

/**
 * 予約の登録・参照・更新・キャンセル・返却処理を行うクラス
 */
public class ReservationService {

    private ArrayList<Reservation> reservationList = new ArrayList<>();
    private int nextId = 1;

    /**
     * CSVから読み込んだデータをセットする（起動時に使用）
     */
    public void setAll(ArrayList<Reservation> list) {
        this.reservationList = list;
        int maxId = 0;
        for (Reservation r : reservationList) {
            if (r.getId() > maxId) {
                maxId = r.getId();
            }
        }
        this.nextId = maxId + 1;
    }

    public ArrayList<Reservation> getAll() {
        return reservationList;
    }

    /**
     * 予約を新規登録する。IDは自動採番。
     */
    public Reservation addReservation(int productId, int customerId,
            LocalDate reservationDate, LocalDate returnPlanDate) {
        Reservation reservation = new Reservation(nextId, productId, customerId, reservationDate, returnPlanDate);
        reservationList.add(reservation);
        nextId++;
        return reservation;
    }

    /**
     * IDで予約を検索する。見つからなければnullを返す。
     */
    public Reservation findById(int id) {
        for (Reservation r : reservationList) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    /**
     * 予約日昇順で並び替えた一覧を返す
     */
    public ArrayList<Reservation> getAllSortedByReservationDate() {
        ArrayList<Reservation> sorted = new ArrayList<>(reservationList);
        Collections.sort(sorted, new Comparator<Reservation>() {
            @Override
            public int compare(Reservation r1, Reservation r2) {
                return r1.getReservationDate().compareTo(r2.getReservationDate());
            }
        });
        return sorted;
    }

    /**
     * 予約日と返却予定日を更新する
     */
    public boolean updateReservation(int id, LocalDate newReservationDate, LocalDate newReturnPlanDate) {
        Reservation reservation = findById(id);
        if (reservation == null) {
            return false;
        }
        reservation.setReservationDate(newReservationDate);
        reservation.setReturnPlanDate(newReturnPlanDate);
        return true;
    }

    /**
     * 予約をキャンセル（削除）する
     */
    public boolean cancelReservation(int id) {
        Reservation reservation = findById(id);
        if (reservation == null) {
            return false;
        }
        reservationList.remove(reservation);
        return true;
    }

    /**
     * 返却日時を記録する
     */
    public boolean returnReservation(int id, LocalDateTime returnDateTime) {
        Reservation reservation = findById(id);
        if (reservation == null) {
            return false;
        }
        reservation.setReturnDateTime(returnDateTime);
        return true;
    }

    /**
     * 指定した物品に、まだ返却されていない予約があるかどうかを判定する
     * 物品削除時の整合性チェックに使用する
     */
    public boolean hasActiveReservationForProduct(int productId) {
        for (Reservation r : reservationList) {
            if (r.getProductId() == productId && r.isActive()) {
                return true;
            }
        }
        return false;
    }
}
