#!/usr/bin/env python3
import os
import sys
import fnmatch
import re

def parse_ignore_file(ignore_path):
    patterns = []
    if not os.path.exists(ignore_path):
        return patterns
    
    with open(ignore_path, 'r', encoding='utf-8', errors='ignore') as f:
        for line in f:
            line = line.strip()
            if not line or line.startswith('#'):
                continue
            # Estandardizar patrones de gitignore para emparejamiento
            patterns.append(line)
    return patterns

def is_ignored(path, rel_path, patterns):
    # Estandarizar rutas con separadores '/'
    norm_rel = rel_path.replace(os.sep, '/')
    
    # Exclusión por defecto de metadatos de Git
    if norm_rel.startswith('.git/') or norm_rel == '.git':
        return True

    for pattern in patterns:
        is_dir_only = pattern.endswith('/')
        pat = pattern.rstrip('/')
        
        # Traducir patrón a regex
        regex_str = re.escape(pat)
        regex_str = regex_str.replace('\\*\\*\\/', '(?:.*/)?').replace('\\*\\*/', '(?:.*/)?')
        regex_str = regex_str.replace('\\*\\*', '.*')
        regex_str = regex_str.replace('\\*', '[^/]*')
        
        if '/' not in pat:
            regex_pat = '^(?:.*/)?' + regex_str
        else:
            regex_pat = '^' + regex_str.lstrip('/')
            
        if is_dir_only:
            regex_pat += '(?:/.*)?$'
        else:
            regex_pat += '$'
            
        if re.search(regex_pat, norm_rel):
            return True
            
    return False

def is_binary(file_path):
    try:
        with open(file_path, 'rb') as f:
            chunk = f.read(1024)
            if b'\x00' in chunk:
                return True
            # Comprobación de decodificación básica
            chunk.decode('utf-8')
    except UnicodeDecodeError:
        return True
    except Exception:
        return True
    return False

def get_comment_style(file_path):
    ext = os.path.splitext(file_path)[1].lower()
    if ext in ['.py', '.sh', '.properties', '.env', '.gitignore', '.properties', '.yml', '.yaml', '.properties']:
        return "# Archivo: {path}\n"
    elif ext in ['.java', '.go', '.js', '.jsx', '.ts', '.tsx', '.css', '.scss', '.json', '.gradle']:
        return "// Archivo: {path}\n"
    elif ext in ['.xml', '.html', '.xhtml', '.svg']:
        return "<!-- Archivo: {path} -->\n"
    else:
        return "/* Archivo: {path} */\n"

def pack_project(project_dir):
    project_dir = os.path.abspath(project_dir)
    print(f"Empaquetando proyecto en: {project_dir}")
    
    ignore_file = os.path.join(project_dir, '.antigravityignore')
    patterns = parse_ignore_file(ignore_file)
    
    # También leer .gitignore si existe para sumar exclusiones básicas
    gitignore_file = os.path.join(project_dir, '.gitignore')
    if os.path.exists(gitignore_file):
        patterns.extend(parse_ignore_file(gitignore_file))
        
    # Asegurar exclusión de codebase_packaged.txt
    patterns.append('**/codebase_packaged.txt')
    
    output_file = os.path.join(project_dir, 'codebase_packaged.txt')
    
    file_count = 0
    with open(output_file, 'w', encoding='utf-8') as out:
        for root, dirs, files in os.walk(project_dir):
            # Filtrar directorios in-situ para optimizar el recorrido recursivo
            filtered_dirs = []
            for d in dirs:
                dir_path = os.path.join(root, d)
                rel_dir_path = os.path.relpath(dir_path, project_dir)
                if not is_ignored(dir_path, rel_dir_path, patterns):
                    filtered_dirs.append(d)
            dirs[:] = filtered_dirs  # Modificar in-situ
            
            for f in files:
                file_path = os.path.join(root, f)
                rel_file_path = os.path.relpath(file_path, project_dir)
                
                if is_ignored(file_path, rel_file_path, patterns):
                    continue
                
                if is_binary(file_path):
                    continue
                
                print(f"  + {rel_file_path}")
                comment_style = get_comment_style(rel_file_path)
                out.write(comment_style.format(path=rel_file_path))
                
                try:
                    with open(file_path, 'r', encoding='utf-8', errors='ignore') as src:
                        out.write(src.read())
                except Exception as e:
                    out.write(f"\n[ERROR LEYENDO ARCHIVO: {e}]\n")
                
                out.write("\n\n")
                file_count += 1
                
    print(f"¡Completado! {file_count} archivos empaquetados en: {output_file}")
    print(f"Tamaño final: {os.path.getsize(output_file) / (1024*1024):.2f} MB")

if __name__ == '__main__':
    target_dir = sys.argv[1] if len(sys.argv) > 1 else os.getcwd()
    pack_project(target_dir)
