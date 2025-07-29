import csv
import mysql.connector

# First: Create a map of state ISO -> StateID from the database
db = mysql.connector.connect(
    host="10.20.1.2",
    user="irontom10",
    password="Mcleod2726",
    database="shop_db_3"
)

cursor = db.cursor()
cursor.execute("SELECT STATE_NAME_ABBREVIATION, STATE_ID FROM STATES")
state_map = {row[0]: row[1] for row in cursor.fetchall()}

seen = set()

with open("full_dataset_csv.csv", newline='') as csvfile:
    reader = csv.DictReader(csvfile)
    for row in reader:
        city = row['city'].strip()
        state_iso = row['stateISO'].strip()

        key = (city, state_iso)
        if key in seen or state_iso not in state_map:
            continue

        seen.add(key)
        state_id = state_map[state_iso]

        safe_city = city.replace("'", "''")  # Escape single quotes for SQL
        print(f"INSERT INTO CITIES (CITY_NAME, STATE_ID) VALUES ('{
              safe_city}', {state_id});")
