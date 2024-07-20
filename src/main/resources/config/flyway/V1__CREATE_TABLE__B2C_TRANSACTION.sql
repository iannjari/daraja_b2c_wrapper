CREATE TABLE b2c_transaction(

    id                  UUID                NOT NULL,
    amount              DECIMAL(20,2)           NOT NULL,
    transaction_ref     VARCHAR(15),
    status              VARCHAR(15),
    terminal            BOOLEAN                 DEFAULT false,
    created_at          TIMESTAMP,
    last_modified       TIMESTAMP               DEFAULT CURRENT_TIMESTAMP  ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT b2c_transaction_pk PRIMARY KEY (id)
);


CREATE UNIQUE INDEX b2c_transaction_id_uindex
    ON b2c_transaction (id);

CREATE UNIQUE INDEX b2c_transaction_trx_ref_uindex
                                ON b2c_transaction (transaction_ref);