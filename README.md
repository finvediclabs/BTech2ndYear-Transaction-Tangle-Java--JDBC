# Transaction-Tangle-Java--JDBC
## Project Overview

This is a small banking ledger demo using Java and JDBC. It supports account creation, transfers, and transaction history.

## Bug Themes

This project intentionally includes the following bugs for educational purposes:

- **Missing transactions/rollbacks:** Transfers are not wrapped in transactions, so partial updates may occur if an error happens.
- **Dirty reads:** No isolation level is set, so concurrent reads may see uncommitted data.
- **Connection leaks:** Connections may not be closed if exceptions occur.
- **Swallowed exceptions:** Some exceptions are caught but not logged or handled, hiding errors.

## How to Fix/Resolve Bugs

### 1. Missing Transactions/Rollbacks
- Use `conn.setAutoCommit(false)` before making changes.
- Wrap updates in a try-catch and call `conn.commit()` on success, `conn.rollback()` on failure.

### 2. Dirty Reads
- Set the connection isolation level: `conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE)` or `Connection.TRANSACTION_REPEATABLE_READ`.

### 3. Connection Leaks
- Always close connections in a `finally` block or use try-with-resources (`try(Connection conn = ...) { ... }`).

### 4. Swallowed Exceptions
- Log exceptions using `e.printStackTrace()` or a logger.
- Propagate or handle exceptions appropriately.

## Running the Project

This demo uses an in-memory H2 database. To run:

1. Compile all Java files in `src/`.
2. Run `Main.java`.

## Educational Purpose

This project is designed to help students identify and fix common JDBC bugs in transactional systems.