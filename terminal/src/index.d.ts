interface KotlinInterface {
  defaultPath: () => string
  validatePath: (arg0: string) => boolean
  getCwd: () => void
  getFiles: (arg0: string) => string
}

interface ProcessExecutorInterface {
  execute: (pwd: string, args: string) => void
  sendToProcess: (data: Uint8Array) => void
}

declare var procEnded: () => void;
declare var readProcResp: (_: string) => void;
declare var setDir: (_: string) => void

declare var terminal: Terminal;
declare var Kotlin: KotlinInterface
declare var ProcessExecutor: ProcessExecutorInterface