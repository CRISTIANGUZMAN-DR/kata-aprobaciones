import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { UsuarioProvider } from './context/UsuarioContext'
import Layout from './components/Layout'
import CrearSolicitud from './pages/CrearSolicitud'
import ListarSolicitudes from './pages/ListarSolicitudes'
import Placeholder from './pages/Placeholder'

export default function App() {
  return (
    <UsuarioProvider>
      <BrowserRouter>
        <Routes>
          <Route element={<Layout />}>
            <Route index element={<CrearSolicitud />} />
            <Route path="solicitudes" element={<ListarSolicitudes />} />
            <Route path="solicitudes/:id" element={<Placeholder titulo="Detalle de solicitud" />} />
            <Route path="notificaciones" element={<Placeholder titulo="Notificaciones" />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </UsuarioProvider>
  )
}
