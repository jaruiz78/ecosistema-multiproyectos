"""
Automated Database Backup & Archiving Manager
Ecosistema Solar Tocina - Los Rosales
Autor: Google Antigravity
"""

import os
import gzip
import shutil
import sqlite3
from datetime import datetime

BACKUP_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "data", "backups")
DB_PATH = os.path.join(os.path.dirname(os.path.abspath(__file__)), "data", "telemetry_history.db")

class BackupManager:
    def __init__(self):
        os.makedirs(BACKUP_DIR, exist_ok=True)

    def create_backup(self):
        """Crea una copia de seguridad SQLite consistente (usando VACUUM INTO o copia limpia) y comprimida en GZIP"""
        if not os.path.exists(DB_PATH):
            return False, "Base de datos no encontrada"

        timestamp_str = datetime.now().strftime("%Y%m%d_%H%M%S")
        temp_backup_file = os.path.join(BACKUP_DIR, f"telemetry_backup_{timestamp_str}.db")
        gz_backup_file = f"{temp_backup_file}.gz"

        try:
            # Usar SQLite online backup API para máxima consistencia ACID
            src_conn = sqlite3.connect(DB_PATH)
            dst_conn = sqlite3.connect(temp_backup_file)
            src_conn.backup(dst_conn)
            dst_conn.close()
            src_conn.close()

            # Comprimir con gzip
            with open(temp_backup_file, "rb") as f_in:
                with gzip.open(gz_backup_file, "wb", compresslevel=9) as f_out:
                    shutil.copyfileobj(f_in, f_out)

            # Eliminar temporal sin comprimir
            if os.path.exists(temp_backup_file):
                os.remove(temp_backup_file)

            size_bytes = os.path.getsize(gz_backup_file)
            size_kb = round(size_bytes / 1024.0, 1)

            # Rotación automática: conservar últimos 30 backups
            self._prune_old_backups(keep=30)

            return True, {
                "file": os.path.basename(gz_backup_file),
                "path": gz_backup_file,
                "size_kb": size_kb,
                "timestamp": datetime.now().isoformat()
            }
        except Exception as e:
            if os.path.exists(temp_backup_file):
                os.remove(temp_backup_file)
            return False, str(e)

    def list_backups(self):
        if not os.path.exists(BACKUP_DIR):
            return []

        files = []
        for fname in sorted(os.listdir(BACKUP_DIR), reverse=True):
            if fname.endswith(".gz") or fname.endswith(".db"):
                fpath = os.path.join(BACKUP_DIR, fname)
                st = os.stat(fpath)
                files.append({
                    "name": fname,
                    "size_kb": round(st.st_size / 1024.0, 1),
                    "created": datetime.fromtimestamp(st.st_mtime).isoformat()
                })
        return files

    def _prune_old_backups(self, keep=30):
        backups = self.list_backups()
        if len(backups) > keep:
            for b in backups[keep:]:
                try:
                    os.remove(os.path.join(BACKUP_DIR, b["name"]))
                except:
                    pass
