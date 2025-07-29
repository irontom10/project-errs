import csv
import mysql.connector

# Connect to DB
conn = mysql.connector.connect(
    host="10.20.1.2",
    user="irontom10",
    password="Mcleod2726",
    database="shop_db_3"
)
cursor = conn.cursor()

# --- 1. Cache COUNTRIES ---
cursor.execute(
    "SELECT COUNTRY_ID, COUNTRY_NAME, COUNTRY_ABBREVIATION FROM COUNTRIES")
# key: COUNTRY_ABBREVIATION (ISO), value: COUNTRY_ID
country_map = {row[2].upper(): row[0] for row in cursor.fetchall()}

# --- 2. Cache STATES ---
cursor.execute(
    "SELECT STATE_ID, STATE_NAME_ABBREVIATION, COUNTRY_ID FROM STATES")
state_map = {(row[1].upper(), row[2]): row[0]
             for row in cursor.fetchall()}  # key: (STATE_ABBR, COUNTRY_ID)

# --- 3. Cache CITIES ---
cursor.execute("SELECT CITY_ID, CITY_NAME, STATE_ID FROM CITIES")
city_map = {(row[1].lower(), row[2]): row[0]
            for row in cursor.fetchall()}  # key: (city.lower(), STATE_ID)

# --- 4. Process CSV ---
added = set()

with open("full_dataset_csv.csv", newline='') as csvfile:
    reader = csv.DictReader(csvfile)
    for row in reader:
        country_iso = row["countryISO"].upper().strip()
        state_iso = row["stateISO"].upper().strip()
        city_name = row["city"].strip()
        zip_code = row["zipCode"].strip()

        # Lookup CountryID
        country_id = country_map.get(country_iso)
        if not country_id:
            continue

        # Lookup StateID
        state_id = state_map.get((state_iso, country_id))
        if not state_id:
            continue

        # Lookup CityID
        city_key = (city_name.lower(), state_id)
        city_id = city_map.get(city_key)
        if not city_id:
            continue

        # Avoid duplicates
        if (zip_code, city_id) in added:
            continue
        added.add((zip_code, city_id))

        # Insert ZIP code
        cursor.execute("""
            INSERT INTO ZIP_CODES (COUNTRY_ID, STATE_ID, CITY_ID, ZIP_CODE)
            VALUES (%s, %s, %s, %s)
        """, (country_id, state_id, city_id, zip_code))

conn.commit()
cursor.close()
conn.close()
