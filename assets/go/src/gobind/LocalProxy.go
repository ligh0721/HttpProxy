package gobind;

import (
	"net"
	"git.tutils.com/tutils/tnet"
)

func StartProxy(agentAddr string, proxyAddr string) {
	clt := tnet.NewTcpClient()
	clt.Addr = agentAddr
	clt.OnDialCallback = func(self *tnet.TcpClient, conn *net.TCPConn) (ok bool, readSize int, connExt interface{}) {
		localProxy := tnet.NewEncryptConnProxy(conn, proxyAddr)
		localProxy.Start()
		return false, 0, localProxy
	}
	clt.Start()
}