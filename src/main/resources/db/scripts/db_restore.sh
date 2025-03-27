#!/bin/bash

# Database credentials
DB_USER="postgres"
DB_HOST="localhost"
DB_PORT="5432"
DB_NAME="finderai_db_restored"
BACKUP_DIR="$(dirname "$0")/backup"  # Backup folder in the same directory as the script
LATEST_BACKUP=$(ls -t "$BACKUP_DIR" | head -n 1)  # Get the latest backup file

# Ensure a backup file exists
if [ -z "$LATEST_BACKUP" ]; then
  echo "No backup file found in $BACKUP_DIR!"
  exit 1
fi

BACKUP_FILE="$BACKUP_DIR/$LATEST_BACKUP"

# Create the database if it doesn’t exist
createdb -U $DB_USER -h $DB_HOST -p $DB_PORT $DB_NAME

# Restore the database
pg_restore -U $DB_USER -h $DB_HOST -p $DB_PORT -d $DB_NAME -F c $BACKUP_FILE

# Check if the restore was successful
if [ $? -eq 0 ]; then
  echo "Restore successful from: $BACKUP_FILE into database: $DB_NAME"
else
  echo "Restore failed!"
fi
