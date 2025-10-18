INSERT IGNORE INTO member (username, email, role, status, created_at, modified_at)
VALUES ('system', 'system@zonbeozon.com', 'ADMIN', 'ACTIVE', NOW(), NOW());