import { createContext, useContext, useState } from 'react'
import PropTypes from 'prop-types'

export const USUARIOS_DISPONIBLES = ['jperez', 'mgarcia', 'alopez']

const UsuarioContext = createContext(null)

export function UsuarioProvider({ children }) {
  const [usuario, setUsuario] = useState(USUARIOS_DISPONIBLES[0])

  return (
    <UsuarioContext.Provider value={{ usuario, setUsuario }}>
      {children}
    </UsuarioContext.Provider>
  )
}

UsuarioProvider.propTypes = {
  children: PropTypes.node.isRequired,
}

export function useUsuario() {
  const context = useContext(UsuarioContext)
  if (!context) {
    throw new Error('useUsuario debe usarse dentro de UsuarioProvider')
  }
  return context
}
