package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.validation.BookingValidator;
import ru.practicum.shareit.item.validation.ItemValidator;
import ru.practicum.shareit.user.validation.UserValidator;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserValidator userValidator;
    private final ItemValidator itemValidator;
    private final BookingValidator bookingValidator;
    private static final Sort SORT_DESC = Sort.by("start").descending();

    @Override
    public BookingDto create(Long userId, BookingCreateDto dto) {
        var user = userValidator.validateUserExists(userId);
        var item = itemValidator.validateItemExists(dto.getItemId());
        if (item.getOwner().getId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Нельзя бронировать свою вещь");
        }
        Booking booking = BookingMapper.toModel(user, item, dto);
        booking.setStatus(BookingStatus.WAITING);
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto approve(Long ownerId, Long bookingId, boolean approved) {
        Booking booking = bookingValidator.validateBookingExists(bookingId);
        bookingValidator.validateOwner(booking, ownerId);
        bookingValidator.validateStatusIsWaiting(booking);
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getById(Long userId, Long bookingId) {
        Booking booking = bookingValidator.validateBookingExists(bookingId);
        bookingValidator.validateAccess(booking, userId);
        return BookingMapper.toDto(booking);
    }

    @Override
    public List<BookingDto> findByBooker(Long userId, BookingState state, int page, int pageSize) {
        userValidator.validateUserExists(userId);
        Instant now = Instant.now();
        List<Booking> list = switch (state) {
            case CURRENT -> bookingRepository.findByBookerIdAndStartBeforeAndEndAfter(userId, now, now, SORT_DESC);
            case PAST    -> bookingRepository.findByBookerIdAndEndIsBefore(userId, now, SORT_DESC);
            case FUTURE  -> bookingRepository.findByBookerIdAndStartIsAfter(userId, now, SORT_DESC);
            case WAITING -> bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.WAITING, SORT_DESC);
            case REJECTED-> bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.REJECTED, SORT_DESC);
            case ALL     -> bookingRepository.findByBookerId(userId, SORT_DESC);
        };
        return list.stream().skip((long) page * pageSize).limit(pageSize)
                .map(BookingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> findByOwner(Long userId, BookingState state, int page, int pageSize) {
        userValidator.validateUserExists(userId);
        Instant now = Instant.now();
        List<Booking> list = switch (state) {
            case CURRENT -> bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfter(userId, now, now, SORT_DESC);
            case PAST    -> bookingRepository.findByItemOwnerIdAndEndIsBefore(userId, now, SORT_DESC);
            case FUTURE  -> bookingRepository.findByItemOwnerIdAndStartIsAfter(userId, now, SORT_DESC);
            case WAITING -> bookingRepository.findByItemOwnerIdAndStatus(userId, BookingStatus.WAITING, SORT_DESC);
            case REJECTED-> bookingRepository.findByItemOwnerIdAndStatus(userId, BookingStatus.REJECTED, SORT_DESC);
            case ALL     -> bookingRepository.findByItemOwnerId(userId, SORT_DESC);
        };
        return list.stream().skip((long) page * pageSize).limit(pageSize)
                .map(BookingMapper::toDto).collect(Collectors.toList());
    }
}

