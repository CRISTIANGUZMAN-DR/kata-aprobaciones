import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { useUsuario } from '../context/UsuarioContext'
import { api, ApiError } from '../api/client'

const ESTADO_ESTILO = {
  PENDIENTE: 'bg-amber-100 text-amber-800',
  APROBADO: 'bg-green-100 text-green-800',
  RECHAZADO: 'bg-red-100 text-red-800',
}

export default function DetalleSolicitud() {
  const { id } = useParams()
  const { usuario } = useUsuario()
  const [solicitud, setSolicitud] = useState(null)
  const [comentario, setComentario] = useState('')
  const [error, setError] = useState(null)
  const [procesando, setProcesando] = useState(false)

  function cargar() {
    return api.obtenerSolicitud(usuario, id).then(setSolicitud)
  }

  useEffect(() => {
    cargar()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id, usuario])

  async function decidir(accion) {
    setError(null)
    setProcesando(true)
    try {
      const fn = accion === 'aprobar' ? api.aprobarSolicitud : api.rechazarSolicitud
      await fn(usuario, id, { aprobador: usuario, comentario })
      setComentario('')
      await cargar()
    } catch (err) {
      setError(err instanceof ApiError ? err.message : 'Error al procesar la decisión')
    } finally {
      setProcesando(false)
    }
  }

  if (!solicitud) {
    return <p className="text-slate-500 text-sm">Cargando...</p>
  }

  const puedeDecidir = solicitud.estado === 'PENDIENTE' && solicitud.aprobador === usuario

  return (
    <div className="max-w-2xl space-y-6">
      <div>
        <div className="flex items-center justify-between">
          <h1 className="text-xl font-semibold text-slate-900">{solicitud.titulo}</h1>
          <span className={`text-xs font-medium px-2 py-1 rounded-full ${ESTADO_ESTILO[solicitud.estado]}`}>
            {solicitud.estado}
          </span>
        </div>
        <p className="text-slate-600 mt-2">{solicitud.descripcion}</p>
        <p className="text-sm text-slate-500 mt-2">
          {solicitud.solicitante} → {solicitud.aprobador} · {solicitud.tipo}
        </p>
      </div>

      {puedeDecidir && (
        <div className="border border-slate-200 rounded-md p-4 space-y-3">
          <h2 className="font-medium text-slate-900">Tu decisión</h2>
          <textarea
            placeholder="Comentario (opcional)"
            value={comentario}
            onChange={(e) => setComentario(e.target.value)}
            className="w-full border border-slate-300 rounded-md px-3 py-2"
            rows={2}
          />
          {error && <p className="text-sm text-red-600">{error}</p>}
          <div className="flex gap-2">
            <button
              disabled={procesando}
              onClick={() => decidir('aprobar')}
              className="bg-green-600 text-white px-4 py-2 rounded-md text-sm font-medium disabled:opacity-50"
            >
              Aprobar
            </button>
            <button
              disabled={procesando}
              onClick={() => decidir('rechazar')}
              className="bg-red-600 text-white px-4 py-2 rounded-md text-sm font-medium disabled:opacity-50"
            >
              Rechazar
            </button>
          </div>
        </div>
      )}

      <div>
        <h2 className="font-medium text-slate-900 mb-2">Historial</h2>
        <ul className="space-y-2">
          {solicitud.historial.map((h) => (
            <li key={h.id} className="border border-slate-200 rounded-md p-3 text-sm">
              <div className="flex items-center justify-between">
                <span className="font-medium">{h.estado}</span>
                <span className="text-slate-400">{new Date(h.fecha).toLocaleString()}</span>
              </div>
              <p className="text-slate-500">por {h.realizadoPor}</p>
              {h.comentario && <p className="text-slate-700 mt-1">&ldquo;{h.comentario}&rdquo;</p>}
            </li>
          ))}
        </ul>
      </div>
    </div>
  )
}
