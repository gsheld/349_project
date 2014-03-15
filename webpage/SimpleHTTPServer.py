import SimpleHTTPServer, SocketServer
Handler = SimpleHTTPServer.SimpleHTTPRequestHandler
httpd = SocketServer.TCPServer(("", 8000), Handler)
httpd.serve_forever()