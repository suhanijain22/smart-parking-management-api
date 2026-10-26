CREATE TABLE IF NOT EXISTS parking_slots (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  slot_code VARCHAR(20) NOT NULL UNIQUE,
  occupied BOOLEAN NOT NULL DEFAULT FALSE
);
CREATE TABLE IF NOT EXISTS bookings (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  vehicle_number VARCHAR(20) NOT NULL,
  slot_id BIGINT NOT NULL,
  entry_time DATETIME(6) NOT NULL,
  exit_time DATETIME(6) NULL,
  status VARCHAR(20) NOT NULL,
  payment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  final_fee DECIMAL(10,2) NULL,
  CONSTRAINT fk_booking_slot FOREIGN KEY (slot_id) REFERENCES parking_slots(id),
  INDEX idx_booking_status (status), INDEX idx_booking_exit_time (exit_time)
);
