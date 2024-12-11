INSERT INTO messages (
    unique_message, group_users, template_id, file, type_file, errors,
    message_text, status, created_at, date_status, data
) VALUES (
             'test_db_initialization',
             1,
             1,
             decode('68656c6c6f2062696e6172792064617461', 'hex'),
             'text/plain',
             'No errors',
             'null',
             'SENT',
             CURRENT_TIMESTAMP,
             CURRENT_TIMESTAMP,
             '{"key1": "value1", "key2": "value2"}'
         );

INSERT INTO templates (template_name, template_text)
VALUES
    ('Template 1', 'Hello, $name! Your transaction on $day is confirmed.'),
    ('Template 2', 'Dear $name, the total cost of your order is $cost.');

INSERT INTO users (name, group_id, email)
VALUES
    ('Alice', 1, 'alice@example.com'),
    ('Bob', 1, 'bob@example.com'),
    ('Charlie', 1, 'charlie@example.com'),
    ('David', 2, 'david@example.com'),
    ('Eve', 2, 'eve@example.com'),
    ('Frank', 2, 'frank@example.com');
