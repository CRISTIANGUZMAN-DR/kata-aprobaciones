import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useUsuario, USUARIOS_DISPONIBLES } from '../context/UsuarioContext'
import { api, ApiError } from '../api/client'

const ESTADO_INICIAL = {
  titulo: '',
  descripcion: '',
  aprobador: '',
  tipo: '',
}

export default function CrearSolicitud() {
  const { usuario } = useUsuario()
  const navigate = useNavigate()
  const [tipos, setTipos] = useState([])
  const [form, setForm] = useState(ESTADO_INICIAL)
  const [error, setError] = useState(null)
  const [enviando, setEnviando] = useState(false)

  const aprobadoresDisponibles = USUARIOS_DISPONIBLES.filter((u) => u !== usuario)

  useEffect(() => {
    api.listarTiposSolicitud(usuario)
      .then((data) => {
        setTipos(data)
        setForm((f) => ({ ...f, tipo: data[0] ?? '', aprobador: aprobadoresDisponibles[0] ?? '' }))
      })
      .catch(() => setError('No se pudo cargar el catálogo de tipos'))
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [usuario])

  async function handleSubmit(e) {
    e.preventDefault()
    setError(null)
    setEnviando(true)
    try {
      const solicitud = await api.crearSolicitud(usuario, {
        ...form,
        solicitante: usuario,
      })
      navigate(`/solicitudes/${solicitud.id}`)
    } catch (err) {
      setError(err instanceof ApiError ? err.message : 'Error al crear la solicitud')
    } finally {
      setEnviando(false)
    }
  }

  return (
    <div className="max-w-lg">
      <h1 className="text-xl font-semibold text-slate-900 mb-4">Crear solicitud de aprobación</h1>

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">Título</label>
          <input
            required
            value={form.titulo}
            onChange={(e) => setForm({ ...form, titulo: e.target.value })}
            className="w-full border border-slate-300 rounded-md px-3 py-2"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">Descripción</label>
          <textarea
            required
            value={form.descripcion}
            onChange={(e) => setForm({ ...form, descripcion: e.target.value })}
            className="w-full border border-slate-300 rounded-md px-3 py-2"
            rows={3}
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">Tipo</label>
          <select
            required
            value={form.tipo}
            onChange={(e) => setForm({ ...form, tipo: e.target.value })}
            className="w-full border border-slate-300 rounded-md px-3 py-2"
          >
            {tipos.map((t) => (
              <option key={t} value={t}>{t}</option>
            ))}
          </select>
        </div>

        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">Aprobador</label>
          <select
            required
            value={form.aprobador}
            onChange={(e) => setForm({ ...form, aprobador: e.target.value })}
            className="w-full border border-slate-300 rounded-md px-3 py-2"
          >
            {aprobadoresDisponibles.map((u) => (
              <option key={u} value={u}>{u}</option>
            ))}
          </select>
        </div>

        <p className="text-sm text-slate-500">Solicitante: <strong>{usuario}</strong></p>

        {error && <p className="text-sm text-red-600">{error}</p>}

        <button
          type="submit"
          disabled={enviando}
          className="bg-slate-900 text-white px-4 py-2 rounded-md text-sm font-medium disabled:opacity-50"
        >
          {enviando ? 'Enviando...' : 'Crear solicitud'}
        </button>
      </form>
    </div>
  )
}
