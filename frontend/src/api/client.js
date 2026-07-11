const BASE_URL = '/api'

export class ApiError extends Error {
  constructor(status, detail) {
    super(detail || `Error ${status}`)
    this.status = status
  }
}

async function request(path, { method = 'GET', usuario, body } = {}) {
  const headers = { 'X-Usuario': usuario }
  if (body !== undefined) {
    headers['Content-Type'] = 'application/json'
  }

  const response = await fetch(`${BASE_URL}${path}`, {
    method,
    headers,
    body: body !== undefined ? JSON.stringify(body) : undefined,
  })

  if (!response.ok) {
    const problem = await response.json().catch(() => null)
    throw new ApiError(response.status, problem?.detail)
  }

  if (response.status === 204) {
    return null
  }

  return response.json()
}

export const api = {
  crearSolicitud: (usuario, datos) =>
    request('/solicitudes', { method: 'POST', usuario, body: datos }),

  listarSolicitudes: (usuario, filtros = {}) => {
    const params = new URLSearchParams(filtros)
    const query = params.toString() ? `?${params.toString()}` : ''
    return request(`/solicitudes${query}`, { usuario })
  },

  obtenerSolicitud: (usuario, id) => request(`/solicitudes/${id}`, { usuario }),

  aprobarSolicitud: (usuario, id, datos) =>
    request(`/solicitudes/${id}/aprobar`, { method: 'PATCH', usuario, body: datos }),

  rechazarSolicitud: (usuario, id, datos) =>
    request(`/solicitudes/${id}/rechazar`, { method: 'PATCH', usuario, body: datos }),

  listarNotificaciones: (usuario) => request(`/notificaciones/${usuario}`, { usuario }),

  marcarNotificacionLeida: (usuario, id) =>
    request(`/notificaciones/${id}/leer`, { method: 'PATCH', usuario }),

  listarTiposSolicitud: (usuario) => request('/catalogo/tipos', { usuario }),
}
