import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { useUsuario } from '../context/UsuarioContext'
import { api } from '../api/client'

const ESTADOS = ['', 'PENDIENTE', 'APROBADO', 'RECHAZADO']

const ESTADO_ESTILO = {
  PENDIENTE: 'bg-amber-100 text-amber-800',
  APROBADO: 'bg-green-100 text-green-800',
  RECHAZADO: 'bg-red-100 text-red-800',
}

export default function ListarSolicitudes() {
  const { usuario } = useUsuario()
  const [solicitudes, setSolicitudes] = useState([])
  const [estado, setEstado] = useState('')
  const [soloMisAprobaciones, setSoloMisAprobaciones] = useState(true)
  const [cargando, setCargando] = useState(true)

  useEffect(() => {
    setCargando(true)
    const filtros = {}
    if (estado) filtros.estado = estado
    if (soloMisAprobaciones) filtros.aprobador = usuario

    api.listarSolicitudes(usuario, filtros)
      .then(setSolicitudes)
      .finally(() => setCargando(false))
  }, [usuario, estado, soloMisAprobaciones])

  return (
    <div>
      <h1 className="text-xl font-semibold text-slate-900 mb-4">Solicitudes</h1>

      <div className="flex items-center gap-4 mb-4">
        <label className="text-sm text-slate-600 flex items-center gap-2">
          Estado:
          <select
            value={estado}
            onChange={(e) => setEstado(e.target.value)}
            className="border border-slate-300 rounded-md px-2 py-1"
          >
            {ESTADOS.map((e) => (
              <option key={e} value={e}>{e || 'Todos'}</option>
            ))}
          </select>
        </label>

        <label className="text-sm text-slate-600 flex items-center gap-2">
          <input
            type="checkbox"
            checked={soloMisAprobaciones}
            onChange={(e) => setSoloMisAprobaciones(e.target.checked)}
          />
          Solo donde soy aprobador
        </label>
      </div>

      {cargando ? (
        <p className="text-slate-500 text-sm">Cargando...</p>
      ) : solicitudes.length === 0 ? (
        <p className="text-slate-500 text-sm">No hay solicitudes con esos filtros.</p>
      ) : (
        <ul className="divide-y divide-slate-200 border border-slate-200 rounded-md">
          {solicitudes.map((s) => (
            <li key={s.id}>
              <Link
                to={`/solicitudes/${s.id}`}
                className="flex items-center justify-between px-4 py-3 hover:bg-slate-50"
              >
                <div>
                  <p className="font-medium text-slate-900">{s.titulo}</p>
                  <p className="text-sm text-slate-500">
                    {s.solicitante} → {s.aprobador} · {s.tipo}
                  </p>
                </div>
                <span className={`text-xs font-medium px-2 py-1 rounded-full ${ESTADO_ESTILO[s.estado]}`}>
                  {s.estado}
                </span>
              </Link>
            </li>
          ))}
        </ul>
      )}
    </div>
  )
}
