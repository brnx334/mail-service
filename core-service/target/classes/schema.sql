CREATE TABLE  IF NOT EXISTS messages (
                                         id SERIAL PRIMARY KEY,
                                         unique_message VARCHAR(255),
                                         group_users INTEGER,
                                         template_id INTEGER,
                                         file BYTEA,
                                         type_file VARCHAR(50),
                                         errors TEXT,
                                         message_text TEXT,
                                         status VARCHAR(50),
                                         created_at TIMESTAMP,
                                         date_status TIMESTAMP,
                                         data JSONB
);

CREATE TABLE IF NOT EXISTS templates (
                                         id SERIAL PRIMARY KEY,
                                         template_name VARCHAR(255) NOT NULL,
                                         template_text TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL,
                                     group_id INTEGER NOT NULL,
                                     email VARCHAR(255) NOT NULL
);
