package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.Instant;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // все бронирования пользователя (booker)
    List<Booking> findByBookerId(Long bookerId, Sort sort);
    List<Booking> findByBookerIdAndStartBeforeAndEndAfter(Long bookerId, Instant now1, Instant now2, Sort sort);
    List<Booking> findByBookerIdAndEndIsBefore(Long bookerId, Instant end, Sort sort);
    List<Booking> findByBookerIdAndStartIsAfter(Long bookerId, Instant start, Sort sort);
    List<Booking> findByBookerIdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    // все бронирования по вещам владельца
    List<Booking> findByItemOwnerId(Long ownerId, Sort sort);
    List<Booking> findByItemOwnerIdAndStartBeforeAndEndAfter(Long ownerId, Instant now1, Instant now2, Sort sort);
    List<Booking> findByItemOwnerIdAndEndIsBefore(Long ownerId, Instant end, Sort sort);
    List<Booking> findByItemOwnerIdAndStartIsAfter(Long ownerId, Instant start, Sort sort);
    List<Booking> findByItemOwnerIdAndStatus(Long ownerId, BookingStatus status, Sort sort);
}
