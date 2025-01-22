INSERT INTO train_fare_matrix (dest_stn_id, src_stn_id, ticket_type_id, fare)
VALUES
    (1, 2, 1, 10.00),  -- Regular fare from Station A to Station B
    (1, 2, 2, 15.00),  -- Express fare from Station A to Station B
    (1, 2, 3, 20.00),  -- Premium fare from Station A to Station B

    (1, 3, 1, 15.00),  -- Regular fare from Station A to Station C
    (1, 3, 2, 20.00),  -- Express fare from Station A to Station C
    (1, 3, 3, 25.00),  -- Premium fare from Station A to Station C

    (1, 4, 1, 20.00),  -- Regular fare from Station A to Station D
    (1, 4, 2, 25.00),  -- Express fare from Station A to Station D
    (1, 4, 3, 30.00),  -- Premium fare from Station A to Station D

    (1, 5, 1, 25.00),  -- Regular fare from Station A to Station E
    (1, 5, 2, 30.00),  -- Express fare from Station A to Station E
    (1, 5, 3, 35.00),  -- Premium fare from Station A to Station E

    (2, 1, 1, 10.00),  -- Regular fare from Station B to Station A
    (2, 1, 2, 15.00),  -- Express fare from Station B to Station A
    (2, 1, 3, 20.00),  -- Premium fare from Station B to Station A

    (2, 3, 1, 10.00),  -- Regular fare from Station B to Station C
    (2, 3, 2, 15.00),  -- Express fare from Station B to Station C
    (2, 3, 3, 20.00),  -- Premium fare from Station B to Station C

    (2, 4, 1, 15.00),  -- Regular fare from Station B to Station D
    (2, 4, 2, 20.00),  -- Express fare from Station B to Station D
    (2, 4, 3, 25.00),  -- Premium fare from Station B to Station D

    (2, 5, 1, 20.00),  -- Regular fare from Station B to Station E
    (2, 5, 2, 25.00),  -- Express fare from Station B to Station E
    (2, 5, 3, 30.00),  -- Premium fare from Station B to Station E

    (3, 1, 1, 15.00),  -- Regular fare from Station C to Station A
    (3, 1, 2, 20.00),  -- Express fare from Station C to Station A
    (3, 1, 3, 25.00),  -- Premium fare from Station C to Station A

    (3, 2, 1, 10.00),  -- Regular fare from Station C to Station B
    (3, 2, 2, 15.00),  -- Express fare from Station C to Station B
    (3, 2, 3, 20.00),  -- Premium fare from Station C to Station B

    (3, 4, 1, 10.00),  -- Regular fare from Station C to Station D
    (3, 4, 2, 15.00),  -- Express fare from Station C to Station D
    (3, 4, 3, 20.00),  -- Premium fare from Station C to Station D

    (3, 5, 1, 15.00),  -- Regular fare from Station C to Station E
    (3, 5, 2, 20.00),  -- Express fare from Station C to Station E
    (3, 5, 3, 25.00),  -- Premium fare from Station C to Station E

    (4, 1, 1, 20.00),  -- Regular fare from Station D to Station A
    (4, 1, 2, 25.00),  -- Express fare from Station D to Station A
    (4, 1, 3, 30.00),  -- Premium fare from Station D to Station A

    (4, 2, 1, 15.00),  -- Regular fare from Station D to Station B
    (4, 2, 2, 20.00),  -- Express fare from Station D to Station B
    (4, 2, 3, 25.00),  -- Premium fare from Station D to Station B

    (4, 3, 1, 10.00),  -- Regular fare from Station D to Station C
    (4, 3, 2, 15.00),  -- Express fare from Station D to Station C
    (4, 3, 3, 20.00),  -- Premium fare from Station D to Station C

    (4, 5, 1, 10.00),  -- Regular fare from Station D to Station E
    (4, 5, 2, 15.00),  -- Express fare from Station D to Station E
    (4, 5, 3, 20.00),  -- Premium fare from Station D to Station E

    (5, 1, 1, 25.00),  -- Regular fare from Station E to Station A
    (5, 1, 2, 30.00),  -- Express fare from Station E to Station A
    (5, 1, 3, 35.00),  -- Premium fare from Station E to Station A

    (5, 2, 1, 20.00),  -- Regular fare from Station E to Station B
    (5, 2, 2, 25.00),  -- Express fare from Station E to Station B
    (5, 2, 3, 30.00),  -- Premium fare from Station E to Station B

    (5, 3, 1, 15.00),  -- Regular fare from Station E to Station C
    (5, 3, 2, 20.00),  -- Express fare from Station E to Station C
    (5, 3, 3, 25.00),  -- Premium fare from Station E to Station C

    (5, 4, 1, 10.00),  -- Regular fare from Station E to Station D
    (5, 4, 2, 15.00),  -- Express fare from Station E to Station D
    (5, 4, 3, 20.00);  -- Premium fare from Station E to Station D


INSERT INTO bus_fare_matrix (dest_bus_stop_id, src_bus_stop_id, ticket_type_id, fare)
VALUES
    (1, 2, 1, 10.00),  -- Regular fare from Station A to Station B
    (1, 2, 2, 15.00),  -- Express fare from Station A to Station B
    (1, 2, 3, 20.00),  -- Premium fare from Station A to Station B

    (1, 3, 1, 15.00),  -- Regular fare from Station A to Station C
    (1, 3, 2, 20.00),  -- Express fare from Station A to Station C
    (1, 3, 3, 25.00),  -- Premium fare from Station A to Station C

    (1, 4, 1, 20.00),  -- Regular fare from Station A to Station D
    (1, 4, 2, 25.00),  -- Express fare from Station A to Station D
    (1, 4, 3, 30.00),  -- Premium fare from Station A to Station D

    (1, 5, 1, 25.00),  -- Regular fare from Station A to Station E
    (1, 5, 2, 30.00),  -- Express fare from Station A to Station E
    (1, 5, 3, 35.00),  -- Premium fare from Station A to Station E

    (2, 1, 1, 10.00),  -- Regular fare from Station B to Station A
    (2, 1, 2, 15.00),  -- Express fare from Station B to Station A
    (2, 1, 3, 20.00),  -- Premium fare from Station B to Station A

    (2, 3, 1, 10.00),  -- Regular fare from Station B to Station C
    (2, 3, 2, 15.00),  -- Express fare from Station B to Station C
    (2, 3, 3, 20.00),  -- Premium fare from Station B to Station C

    (2, 4, 1, 15.00),  -- Regular fare from Station B to Station D
    (2, 4, 2, 20.00),  -- Express fare from Station B to Station D
    (2, 4, 3, 25.00),  -- Premium fare from Station B to Station D

    (2, 5, 1, 20.00),  -- Regular fare from Station B to Station E
    (2, 5, 2, 25.00),  -- Express fare from Station B to Station E
    (2, 5, 3, 30.00),  -- Premium fare from Station B to Station E

    (3, 1, 1, 15.00),  -- Regular fare from Station C to Station A
    (3, 1, 2, 20.00),  -- Express fare from Station C to Station A
    (3, 1, 3, 25.00),  -- Premium fare from Station C to Station A

    (3, 2, 1, 10.00),  -- Regular fare from Station C to Station B
    (3, 2, 2, 15.00),  -- Express fare from Station C to Station B
    (3, 2, 3, 20.00),  -- Premium fare from Station C to Station B

    (3, 4, 1, 10.00),  -- Regular fare from Station C to Station D
    (3, 4, 2, 15.00),  -- Express fare from Station C to Station D
    (3, 4, 3, 20.00),  -- Premium fare from Station C to Station D

    (3, 5, 1, 15.00),  -- Regular fare from Station C to Station E
    (3, 5, 2, 20.00),  -- Express fare from Station C to Station E
    (3, 5, 3, 25.00),  -- Premium fare from Station C to Station E

    (4, 1, 1, 20.00),  -- Regular fare from Station D to Station A
    (4, 1, 2, 25.00),  -- Express fare from Station D to Station A
    (4, 1, 3, 30.00),  -- Premium fare from Station D to Station A

    (4, 2, 1, 15.00),  -- Regular fare from Station D to Station B
    (4, 2, 2, 20.00),  -- Express fare from Station D to Station B
    (4, 2, 3, 25.00),  -- Premium fare from Station D to Station B

    (4, 3, 1, 10.00),  -- Regular fare from Station D to Station C
    (4, 3, 2, 15.00),  -- Express fare from Station D to Station C
    (4, 3, 3, 20.00),  -- Premium fare from Station D to Station C

    (4, 5, 1, 10.00),  -- Regular fare from Station D to Station E
    (4, 5, 2, 15.00),  -- Express fare from Station D to Station E
    (4, 5, 3, 20.00),  -- Premium fare from Station D to Station E

    (5, 1, 1, 25.00),  -- Regular fare from Station E to Station A
    (5, 1, 2, 30.00),  -- Express fare from Station E to Station A
    (5, 1, 3, 35.00),  -- Premium fare from Station E to Station A

    (5, 2, 1, 20.00),  -- Regular fare from Station E to Station B
    (5, 2, 2, 25.00),  -- Express fare from Station E to Station B
    (5, 2, 3, 30.00),  -- Premium fare from Station E to Station B

    (5, 3, 1, 15.00),  -- Regular fare from Station E to Station C
    (5, 3, 2, 20.00),  -- Express fare from Station E to Station C
    (5, 3, 3, 25.00),  -- Premium fare from Station E to Station C

    (5, 4, 1, 10.00),  -- Regular fare from Station E to Station D
    (5, 4, 2, 15.00),  -- Express fare from Station E to Station D
    (5, 4, 3, 20.00);  -- Premium fare from Station E to Station D
