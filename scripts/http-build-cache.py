#!/usr/bin/env python3
"""Cache de build Gradle HTTP minimal (« bucket HTTP simple », plan-action §2) pour la CI et le bench.

Protocole du HttpBuildCache Gradle : GET /cache/<clé> → 200 + corps ou 404 ; PUT /cache/<clé> → stocke.
GET /stats → compteurs JSON (hits, misses, stores). Aucun contrôle d'accès : n'écoute que sur 127.0.0.1.
Pour un cache partagé entre postes et CI, utiliser un vrai serveur (Develocity build cache node, bucket
derrière HTTPS) et GRADLE_REMOTE_CACHE_URL / _USERNAME / _PASSWORD (settings.gradle.kts).

Usage : http-build-cache.py <port> <dossier>
"""
import json
import os
import re
import sys
import tempfile
import threading
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer

PORT = int(sys.argv[1]) if len(sys.argv) > 1 else 5071
ROOT = os.path.abspath(sys.argv[2] if len(sys.argv) > 2 else "http-build-cache")
KEY = re.compile(r"^/cache/([0-9A-Za-z_-]{1,128})$")
STATS = {"hits": 0, "misses": 0, "stores": 0}
LOCK = threading.Lock()


def count(name):
    with LOCK:
        STATS[name] += 1


class Handler(BaseHTTPRequestHandler):
    protocol_version = "HTTP/1.1"

    def _entry(self):
        m = KEY.match(self.path)
        return os.path.join(ROOT, m.group(1)) if m else None

    def _reply(self, code, body=b"", ctype="application/octet-stream"):
        self.send_response(code)
        self.send_header("Content-Type", ctype)
        self.send_header("Content-Length", str(len(body)))
        self.end_headers()
        if body and self.command != "HEAD":
            self.wfile.write(body)

    def _read_body(self):
        if self.headers.get("Transfer-Encoding", "").lower() == "chunked":
            chunks = []
            while True:
                size = int(self.rfile.readline().split(b";")[0].strip(), 16)
                if size == 0:
                    self.rfile.readline()
                    return b"".join(chunks)
                chunks.append(self.rfile.read(size))
                self.rfile.readline()
        return self.rfile.read(int(self.headers.get("Content-Length", "0")))

    def do_GET(self):
        if self.path == "/stats":
            with LOCK:
                body = json.dumps(STATS).encode()
            return self._reply(200, body, "application/json")
        entry = self._entry()
        if entry and os.path.isfile(entry):
            count("hits")
            with open(entry, "rb") as f:
                return self._reply(200, f.read())
        count("misses")
        return self._reply(404)

    do_HEAD = do_GET

    def do_PUT(self):
        entry = self._entry()
        body = self._read_body()
        if entry is None:
            return self._reply(400)
        fd, tmp = tempfile.mkstemp(dir=ROOT)
        with os.fdopen(fd, "wb") as f:
            f.write(body)
        os.replace(tmp, entry)
        count("stores")
        return self._reply(201)

    def log_message(self, *args):
        pass


if __name__ == "__main__":
    os.makedirs(ROOT, exist_ok=True)
    print(f"cache HTTP sur http://127.0.0.1:{PORT}/cache/ → {ROOT}", flush=True)
    ThreadingHTTPServer(("127.0.0.1", PORT), Handler).serve_forever()
