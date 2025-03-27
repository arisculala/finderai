#!/bin/bash

# Database credentials
DB_USER="postgres"
DB_HOST="localhost"
DB_PORT="5432"
DB_NAME="finderai_db"
BACKUP_DIR="$(dirname "$0")/backup"  # Backup folder in the same directory as the script
BACKUP_FILE="$BACKUP_DIR/finderai_backup_$(date +%Y%m%d%H%M%S).dump"

# Ensure the backup directory exists
mkdir -p "$BACKUP_DIR"

# Perform the backup
pg_dump -U $DB_USER -h $DB_HOST -p $DB_PORT -d $DB_NAME -F c -f $BACKUP_FILE

# Check if the backup was successful
if [ $? -eq 0 ]; then
  echo "Backup successful: $BACKUP_FILE"
else
  echo "Backup failed!"
fi
