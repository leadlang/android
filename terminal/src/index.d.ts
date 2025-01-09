interface KotlinInterface {
  defaultPath: () => string
  validatePath: (arg0: string) => boolean
  getCwd: () => void
  getFiles: (arg0: string) => string
}

declare var setDir: (_: string) => void
declare var Kotlin: KotlinInterface