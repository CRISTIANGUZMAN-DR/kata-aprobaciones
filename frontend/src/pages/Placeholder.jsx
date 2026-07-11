import PropTypes from 'prop-types'

export default function Placeholder({ titulo }) {
  return (
    <div className="rounded-lg border border-dashed border-slate-300 p-8 text-center text-slate-500">
      <p className="font-medium">{titulo}</p>
      <p className="text-sm">Esta sección todavía está en construcción.</p>
    </div>
  )
}

Placeholder.propTypes = {
  titulo: PropTypes.string.isRequired,
}
