MERGE INTO member (username, email, role, status, created_at, modified_at)
KEY(username)
VALUES ('system', 'system@zonbeozon.com', 'ADMIN', 'ACTIVE', NOW(), NOW());