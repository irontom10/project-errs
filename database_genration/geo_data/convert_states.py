import csv

seen = set()
with open("full_dataset_csv.csv", newline='') as csvfile:
    reader = csv.DictReader(csvfile)
    for row in reader:
        if row['countryISO'] == 'CA':
            key = (row['state'], row['stateISO'])
            if key not in seen:
                seen.add(key)
                print(f"INSERT INTO STATES (STATE_NAME, STATE_NAME_ABBREVIATION, COUNTRY_ID) VALUES ('{
                      row['state']}', '{row['stateISO']}', 2);")
