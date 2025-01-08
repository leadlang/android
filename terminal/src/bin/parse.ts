import { Terminal } from "@xterm/xterm";

export function parse($path: string, path: (_: string) => void, terminal: Terminal, cmd: string, args: string[]) {
  switch (cmd) {
    case "clear":
      terminal.clear();
      break;
    case "dir":
      if ($path == "") {
        terminal.write(`\x1b[31mError: No path selected\x1b[m`);
        break;
      }

      break;
    case "cd":
      const newPath = args.join(" ");

      if (!Kotlin.validatePath(newPath)) {
        terminal.write(`\x1b[31mError: Invalid Path\x1b[m`);
        break;
      }

      path(newPath)

      break;
    case "cdui":
      terminal.writeln("Contacting UI provider...");
      break;
    case "cat":
      terminal.writeln($path)
      break;
    default:
      throw new Error(`Cannot find ${cmd}`)
  }
}