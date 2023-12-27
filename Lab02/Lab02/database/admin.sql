INSERT INTO user_role (name) VALUES ('ADMIN'), ('WORKER');
COMMIT;

INSERT INTO app_user (login, password, role_id, worker_id)
VALUES ('admin', '$2a$10$HHQTFMsMTFG646f2CoxUseYbut.4BEcj7C5SjG9edLfjI.qUMKyWa', 1, NULL);
COMMIT;