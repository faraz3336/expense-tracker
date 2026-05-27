package com.fullStack.expenseTracker.repository;

import com.fullStack.expenseTracker.dto.reponses.TransactionsMonthlySummaryDto;
import com.fullStack.expenseTracker.models.Transaction;
import com.fullStack.expenseTracker.models.TransactionType;
import com.fullStack.expenseTracker.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("""
            SELECT t
            FROM Transaction t
            JOIN t.user u
            LEFT JOIN t.category c
            LEFT JOIN c.transactionType tt
            WHERE u.email = :email
              AND (:transactionType = '' OR LOWER(CAST(tt.transactionTypeName AS string)) LIKE LOWER(CONCAT('%', :transactionType, '%')))
              AND (:searchKey = '' OR LOWER(COALESCE(t.description, '')) LIKE LOWER(CONCAT('%', :searchKey, '%'))
                   OR LOWER(COALESCE(c.categoryName, '')) LIKE LOWER(CONCAT('%', :searchKey, '%')))
            """)
    Page<Transaction> findByUser(@Param("email") String email,
                                 Pageable pageable,
                                 @Param("searchKey") String searchKey,
                                 @Param("transactionType") String transactionType);

    @Query(value = "SELECT t.*, c.category_id AS c_category_id, c.category_name AS c_category_name, " +
            "u.id AS u_id, u.email AS u_email, " +
            "tt.transaction_type_id AS tt_transaction_type_id, tt.transaction_type_name AS tt_transaction_type_name " +
            "FROM transactions t JOIN category c ON t.category_id = c.category_id JOIN users u ON t.user_id = u.id " +
            "JOIN transaction_type tt ON c.transaction_type_id = tt.transaction_type_id " +
            "WHERE t.description LIKE %:searchKey% OR c.category_name LIKE %:searchKey% OR " +
            "tt.transaction_type_name LIKE %:searchKey% OR u.email LIKE %:searchKey%", nativeQuery = true)
    Page<Transaction> findAll(Pageable pageable, @Param("searchKey") String searchKey);


    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            LEFT JOIN t.category c
            LEFT JOIN c.transactionType tt
            WHERE t.user.id = :userId
              AND tt.transactionTypeId = :transactionTypeId
              AND MONTH(t.date) = :month
              AND YEAR(t.date) = :year
            """)
    Double findTotalByUserAndTransactionType(@Param("userId") long userId,
                                             @Param("transactionTypeId") Integer transactionTypeId,
                                             @Param("month") int month,
                                             @Param("year") int year);

    @Query("""
            SELECT COUNT(t)
            FROM Transaction t
            WHERE t.user.id = :userId
              AND MONTH(t.date) = :month
              AND YEAR(t.date) = :year
            """)
    Long findTotalNoOfTransactionsByUser(@Param("userId") long userId, @Param("month") int month, @Param("year") int year);

    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            LEFT JOIN t.category c
            WHERE t.user.email = :email
              AND c.categoryId = :categoryId
              AND MONTH(t.date) = :month
              AND YEAR(t.date) = :year
            """)
    Double findTotalByUserAndCategory(@Param("email") String email,
                                      @Param("categoryId") int categoryId,
                                      @Param("month") int month,
                                      @Param("year") int year);

    @Query("""
            SELECT MONTH(t.date),
                   COALESCE(SUM(CASE WHEN tt.transactionTypeId = 1 THEN t.amount ELSE 0 END), 0),
                   COALESCE(SUM(CASE WHEN tt.transactionTypeId = 2 THEN t.amount ELSE 0 END), 0)
            FROM Transaction t
            LEFT JOIN t.category c
            LEFT JOIN c.transactionType tt
            WHERE t.user.email = :email
              AND t.date >= :cutoffDate
            GROUP BY YEAR(t.date), MONTH(t.date)
            ORDER BY YEAR(t.date), MONTH(t.date)
            """)
    List<Object[]> findMonthlySummaryByUser(@Param("email") String email, @Param("cutoffDate") LocalDate cutoffDate);
}
