#!/usr/bin/env python3
"""
download_academic_open_papers.py
-------------------------------------------------------------------------
Descargador Masivo Automatizado de Papers Académicos Fundacionales (Nivel Ph.D. y Base Noveles)
Descarga fuentes primarias que cubren desde los fundamentos de ciencias computacionales
hasta las fronteras de IA, Big Data, Sistemas Distribuidos, Matemáticas y Hardware.
-------------------------------------------------------------------------
"""
import os
import sys
import time
import json
import urllib.request
from pathlib import Path

BASE_DIR = Path("/home/jaruiz/Desarrollo/docs/formacion_ecosistema/biblioteca_papers_pdf_rfc")

OPEN_ACCESS_PAPERS = [
    # =========================================================================
    # FACULTAD I: SOFTWARE ENG, DDD PURO & LENGUAJES (CMU / STANFORD)
    # =========================================================================
    {
        "faculty": "01_software_eng_ddd_tipos",
        "filename": "2006_moseley_out_of_the_tar_pit.pdf",
        "title": "Out of the Tar Pit (Complexity & State Management)",
        "authors": ["Ben Moseley", "Peter Marks"],
        "year": 2006,
        "institution": "Software Craftsmanship Guild",
        "urls": [
            "https://raw.githubusercontent.com/papers-we-love/papers-we-love/master/design/out-of-the-tar-pit.pdf"
        ]
    },
    {
        "faculty": "01_software_eng_ddd_tipos",
        "filename": "1986_brooks_no_silver_bullet.pdf",
        "title": "No Silver Bullet - Essence and Accidents of Software Engineering",
        "authors": ["Frederick P. Brooks Jr."],
        "year": 1986,
        "institution": "UNC Chapel Hill / IEEE Computer",
        "urls": [
            "https://raw.githubusercontent.com/papers-we-love/papers-we-love/master/engineering/no-silver-bullet.pdf",
            "http://faculty.salisbury.edu/~xswang/Research/Papers/SERef/no-silver-bullet.pdf"
        ]
    },
    {
        "faculty": "01_software_eng_ddd_tipos",
        "filename": "1968_dijkstra_goto_considered_harmful.pdf",
        "title": "Go To Statement Considered Harmful",
        "authors": ["Edsger W. Dijkstra"],
        "year": 1968,
        "institution": "Eindhoven University of Technology / CACM",
        "urls": [
            "https://raw.githubusercontent.com/papers-we-love/papers-we-love/master/programming_languages/go-to-statement-considered-harmful.pdf",
            "https://homepages.cwi.nl/~storm/teaching/reader/Dijkstra68.pdf"
        ]
    },
    {
        "faculty": "01_software_eng_ddd_tipos",
        "filename": "1969_hoare_axiomatic_basis.pdf",
        "title": "An Axiomatic Basis for Computer Programming (Hoare Logic)",
        "authors": ["C. A. R. Hoare"],
        "year": 1969,
        "institution": "Queen's University of Belfast / CACM",
        "urls": [
            "https://raw.githubusercontent.com/papers-we-love/papers-we-love/master/cs_theory/an-axiomatic-basis-for-computer-programming.pdf",
            "https://www.cs.cmu.edu/~crary/819-f09/Hoare69.pdf"
        ]
    },

    # =========================================================================
    # FACULTAD II: SISTEMAS DISTRIBUIDOS, CONSENSO & TLA+ (MIT / BERKELEY)
    # =========================================================================
    {
        "faculty": "02_sistemas_distribuidos_consenso",
        "filename": "2014_ongaro_raft_consensus_extended.pdf",
        "title": "In Search of an Understandable Consensus Algorithm (Raft Extended)",
        "authors": ["Diego Ongaro", "John Ousterhout"],
        "year": 2014,
        "institution": "Stanford University",
        "urls": ["https://raft.github.io/raft.pdf"]
    },
    {
        "faculty": "02_sistemas_distribuidos_consenso",
        "filename": "2001_lamport_paxos_made_simple.pdf",
        "title": "Paxos Made Simple",
        "authors": ["Leslie Lamport"],
        "year": 2001,
        "institution": "Microsoft Research",
        "urls": ["https://lamport.azurewebsites.net/pubs/paxos-simple.pdf"]
    },
    {
        "faculty": "02_sistemas_distribuidos_consenso",
        "filename": "1985_fischer_lynch_paterson_flp_impossibility.pdf",
        "title": "Impossibility of Distributed Consensus with One Faulty Process (FLP)",
        "authors": ["Michael J. Fischer", "Nancy A. Lynch", "Michael S. Paterson"],
        "year": 1985,
        "institution": "Yale University / MIT (JACM)",
        "urls": ["https://groups.csail.mit.edu/tds/papers/Lynch/jacm85.pdf"]
    },
    {
        "faculty": "02_sistemas_distribuidos_consenso",
        "filename": "1990_herlihy_wing_linearizability.pdf",
        "title": "Linearizability: A Correctness Condition for Concurrent Objects",
        "authors": ["Maurice P. Herlihy", "Jeannette M. Wing"],
        "year": 1990,
        "institution": "Carnegie Mellon University / ACM TOPLAS",
        "urls": [
            "https://cs.brown.edu/~mph/HerlihyW90/p463-herlihy.pdf",
            "https://www.cs.cmu.edu/~wing/publications/HerlihyWing90.pdf"
        ]
    },
    {
        "faculty": "02_sistemas_distribuidos_consenso",
        "filename": "2006_burrows_chubby_lock_service.pdf",
        "title": "The Chubby Lock Service for Loosely-Coupled Distributed Systems",
        "authors": ["Mike Burrows"],
        "year": 2006,
        "institution": "Google / OSDI",
        "urls": ["https://static.googleusercontent.com/media/research.google.com/en//archive/chubby-osdi06.pdf"]
    },

    # =========================================================================
    # FACULTAD III: RUNTIME JVM, HARDWARE & MEMORIA (ORACLE / ETH / RED HAT)
    # =========================================================================
    {
        "faculty": "03_runtime_jvm_memoria",
        "filename": "2007_drepper_what_every_programmer_should_know_about_memory.pdf",
        "title": "What Every Programmer Should Know About Memory (CPU Caches, NUMA, TLB)",
        "authors": ["Ulrich Drepper"],
        "year": 2007,
        "institution": "Red Hat",
        "urls": [
            "https://people.freebsd.org/~lstewart/articles/cpumemory.pdf",
            "https://raw.githubusercontent.com/papers-we-love/papers-we-love/master/systems/what-every-programmer-should-know-about-memory.pdf"
        ]
    },
    {
        "faculty": "03_runtime_jvm_memoria",
        "filename": "2018_hennessy_patterson_new_golden_age_computer_architecture.pdf",
        "title": "A New Golden Age for Computer Architecture: Domain-Specific Accelerators",
        "authors": ["John L. Hennessy", "David A. Patterson"],
        "year": 2018,
        "institution": "Stanford / UC Berkeley / ACM Turing Lecture",
        "urls": [
            "https://raw.githubusercontent.com/papers-we-love/papers-we-love/master/hardware/a-new-golden-age-for-computer-architecture.pdf",
            "https://cacm.acm.org/system/assets/0003/1879/011719_CACMpg48_A-New-Golden-Age.pdf"
        ]
    },

    # =========================================================================
    # FACULTAD IV: CONCURRENCIA GO, RUNTIME & CSP (OXFORD / GOOGLE)
    # =========================================================================
    {
        "faculty": "04_concurrencia_go_csp",
        "filename": "1978_hoare_csp.pdf",
        "title": "Communicating Sequential Processes (CSP)",
        "authors": ["C. A. R. Hoare"],
        "year": 1978,
        "institution": "Oxford University / CACM",
        "urls": ["https://www.cs.cmu.edu/~crary/819-f09/Hoare78.pdf"]
    },
    {
        "faculty": "04_concurrencia_go_csp",
        "filename": "2011_thompson_lmax_disruptor.pdf",
        "title": "The LMAX Disruptor: High Performance Alternative to Bounded Queues",
        "authors": ["Martin Thompson", "Dave Farley", "Michael Barker"],
        "year": 2011,
        "institution": "LMAX Exchange",
        "urls": ["https://lmax-exchange.github.io/disruptor/files/Disruptor-1.0.pdf"]
    },

    # =========================================================================
    # FACULTAD V: GEMELO DIGITAL, MATEMÁTICAS, FÍSICA & TEORÍA DE LA INFORMACIÓN
    # =========================================================================
    {
        "faculty": "05_gemelo_digital_tensores_enkf",
        "filename": "1948_shannon_mathematical_theory_of_communication.pdf",
        "title": "A Mathematical Theory of Communication (Information Theory & Entropy)",
        "authors": ["Claude E. Shannon"],
        "year": 1948,
        "institution": "Bell System Technical Journal",
        "urls": [
            "https://people.math.harvard.edu/~ctm/home/text/others/shannon/entropy/entropy.pdf",
            "https://raw.githubusercontent.com/papers-we-love/papers-we-love/master/information_theory/a-mathematical-theory-of-communication.pdf"
        ]
    },
    {
        "faculty": "05_gemelo_digital_tensores_enkf",
        "filename": "1950_nash_equilibrium_points_in_n_person_games.pdf",
        "title": "Equilibrium Points in N-Person Games (Nash Equilibrium)",
        "authors": ["John F. Nash"],
        "year": 1950,
        "institution": "Princeton University / PNAS",
        "urls": [
            "https://raw.githubusercontent.com/papers-we-love/papers-we-love/master/math/equilibrium-points-in-n-person-games.pdf",
            "https://www.pnas.org/doi/pdf/10.1073/pnas.36.1.48"
        ]
    },
    {
        "faculty": "05_gemelo_digital_tensores_enkf",
        "filename": "2008_verstraete_peps_tensor_networks.pdf",
        "title": "Matrix Product States and Projected Entangled Pair States (PEPS)",
        "authors": ["Frank Verstraete", "Valentin Murg", "J. Ignacio Cirac"],
        "year": 2008,
        "institution": "Princeton IAS / Max-Planck-Institut / Advances in Physics",
        "urls": ["https://arxiv.org/pdf/0705.2260.pdf"]
    },
    {
        "faculty": "05_gemelo_digital_tensores_enkf",
        "filename": "2019_raissi_pinns_deep_learning.pdf",
        "title": "Physics-Informed Neural Networks (PINNs): Deep Learning for PDEs",
        "authors": ["Maziar Raissi", "Paris Perdikaris", "George Em Karniadakis"],
        "year": 2019,
        "institution": "Brown University / Journal of Computational Physics",
        "urls": ["https://arxiv.org/pdf/1711.10561.pdf"]
    },
    {
        "faculty": "05_gemelo_digital_tensores_enkf",
        "filename": "2017_arjovsky_wasserstein_gan_optimal_transport.pdf",
        "title": "Wasserstein GAN (Optimal Transport & Earth Mover Distance)",
        "authors": ["Martin Arjovsky", "Soumith Chintala", "Léon Bottou"],
        "year": 2017,
        "institution": "Courant Institute / Facebook AI Research / ICML",
        "urls": ["https://arxiv.org/pdf/1701.07875.pdf"]
    },

    # =========================================================================
    # FACULTAD VI: IA GENERATIVA, TRANSFORMERS, RAG, LORA & EDGE LITERt
    # =========================================================================
    {
        "faculty": "06_edge_ai_litert_neurosimbolico",
        "filename": "2017_vaswani_attention_is_all_you_need.pdf",
        "title": "Attention Is All You Need (Transformers Architecture)",
        "authors": ["Ashish Vaswani", "Noam Shazeer", "Niki Parmar et al."],
        "year": 2017,
        "institution": "Google Brain / Google Research / NeurIPS",
        "urls": ["https://arxiv.org/pdf/1706.03762.pdf"]
    },
    {
        "faculty": "06_edge_ai_litert_neurosimbolico",
        "filename": "2020_lewis_retrieval_augmented_generation_rag.pdf",
        "title": "Retrieval-Augmented Generation for Knowledge-Intensive NLP Tasks (RAG)",
        "authors": ["Patrick Lewis", "Ethan Perez", "Aleksandra Piktus et al."],
        "year": 2020,
        "institution": "Facebook AI Research / UCL / NYU / NeurIPS",
        "urls": ["https://arxiv.org/pdf/2005.11401.pdf"]
    },
    {
        "faculty": "06_edge_ai_litert_neurosimbolico",
        "filename": "2021_hu_lora_low_rank_adaptation.pdf",
        "title": "LoRA: Low-Rank Adaptation of Large Language Models",
        "authors": ["Edward J. Hu", "Yelong Shen", "Phillip Wallis et al."],
        "year": 2021,
        "institution": "Microsoft Corporation / ICLR",
        "urls": ["https://arxiv.org/pdf/2106.09685.pdf"]
    },
    {
        "faculty": "06_edge_ai_litert_neurosimbolico",
        "filename": "2017_shazeer_mixture_of_experts_moe.pdf",
        "title": "Outrageously Large Neural Networks: The Sparsely-Gated Mixture-of-Experts Layer",
        "authors": ["Noam Shazeer", "Azalia Mirhoseini", "Krzysztof Maziarz et al."],
        "year": 2017,
        "institution": "Google Brain / ICLR",
        "urls": ["https://arxiv.org/pdf/1701.06538.pdf"]
    },
    {
        "faculty": "06_edge_ai_litert_neurosimbolico",
        "filename": "2018_jacob_integer_quantization_inference.pdf",
        "title": "Quantization and Training of Neural Networks for Integer-Arithmetic Inference (LiteRT)",
        "authors": ["Benoit Jacob et al."],
        "year": 2018,
        "institution": "Google Research / CVPR",
        "urls": ["https://arxiv.org/pdf/1712.05877.pdf"]
    },
    {
        "faculty": "06_edge_ai_litert_neurosimbolico",
        "filename": "2018_malkov_hnsw_vector_search.pdf",
        "title": "Efficient and Robust Approximate Nearest Neighbor Search Using HNSW",
        "authors": ["Yu. A. Malkov", "D. A. Yashunin"],
        "year": 2018,
        "institution": "IEEE TPAMI",
        "urls": ["https://arxiv.org/pdf/1603.09320.pdf"]
    },

    # =========================================================================
    # FACULTAD VII: BIG DATA, BASES DE DATOS RELACIONALES, SPANNER & CLOUD NATIVE
    # =========================================================================
    {
        "faculty": "07_cloud_bigquery_finops",
        "filename": "1970_codd_relational_model_for_large_shared_databanks.pdf",
        "title": "A Relational Model of Data for Large Shared Data Banks",
        "authors": ["Edgar F. Codd"],
        "year": 1970,
        "institution": "IBM Research / CACM",
        "urls": [
            "https://raw.githubusercontent.com/papers-we-love/papers-we-love/master/databases/a-relational-model-of-data-for-large-shared-data-banks.pdf",
            "https://www.seas.upenn.edu/~zives/03f/cis550/codd.pdf"
        ]
    },
    {
        "faculty": "07_cloud_bigquery_finops",
        "filename": "1981_gray_transaction_concept_acid.pdf",
        "title": "The Transaction Concept: Virtues and Limitations (ACID Principles)",
        "authors": ["Jim Gray"],
        "year": 1981,
        "institution": "Tandem Computers / VLDB",
        "urls": [
            "https://raw.githubusercontent.com/papers-we-love/papers-we-love/master/databases/the-transaction-concept-virtues-and-limitations.pdf",
            "http://www.hpl.hp.com/techreports/tandem/TR-81.3.pdf"
        ]
    },
    {
        "faculty": "07_cloud_bigquery_finops",
        "filename": "2012_corbett_dean_spanner_database.pdf",
        "title": "Spanner: Google’s Globally-Distributed Database (TrueTime & Paxos)",
        "authors": ["James C. Corbett", "Jeffrey Dean", "Sanjay Ghemawat et al."],
        "year": 2012,
        "institution": "Google Research / OSDI",
        "urls": ["https://static.googleusercontent.com/media/research.google.com/en//archive/spanner-osdi2012.pdf"]
    },
    {
        "faculty": "07_cloud_bigquery_finops",
        "filename": "2010_melnik_dremel_interactive_analysis.pdf",
        "title": "Dremel: Interactive Analysis of Web-Scale Datasets (BigQuery Capacitor)",
        "authors": ["Sergey Melnik et al."],
        "year": 2010,
        "institution": "Google Research / VLDB",
        "urls": ["https://static.googleusercontent.com/media/research.google.com/en//pubs/archive/36632.pdf"]
    },
    {
        "faculty": "07_cloud_bigquery_finops",
        "filename": "2004_dean_ghemawat_mapreduce.pdf",
        "title": "MapReduce: Simplified Data Processing on Large Clusters",
        "authors": ["Jeffrey Dean", "Sanjay Ghemawat"],
        "year": 2004,
        "institution": "Google / OSDI",
        "urls": ["https://static.googleusercontent.com/media/research.google.com/en//archive/mapreduce-osdi04.pdf"]
    },
    {
        "faculty": "07_cloud_bigquery_finops",
        "filename": "2006_chang_bigtable_distributed_storage.pdf",
        "title": "Bigtable: A Distributed Storage System for Structured Data",
        "authors": ["Fay Chang", "Jeffrey Dean", "Sanjay Ghemawat et al."],
        "year": 2006,
        "institution": "Google / OSDI",
        "urls": ["https://static.googleusercontent.com/media/research.google.com/en//archive/bigtable-osdi06.pdf"]
    },
    {
        "faculty": "07_cloud_bigquery_finops",
        "filename": "2015_verma_google_borg_cluster_management.pdf",
        "title": "Large-Scale Cluster Management at Google with Borg",
        "authors": ["Abhishek Verma et al."],
        "year": 2015,
        "institution": "Google / EuroSys",
        "urls": ["https://static.googleusercontent.com/media/research.google.com/en//pubs/archive/43438.pdf"]
    },
    {
        "faculty": "07_cloud_bigquery_finops",
        "filename": "1996_oneil_lsm_tree.pdf",
        "title": "The Log-Structured Merge-Tree (LSM-Tree)",
        "authors": ["Patrick O'Neil", "Edward O'Neil", "Gerhard Weikum"],
        "year": 1996,
        "institution": "UMass Boston / Acta Informatica",
        "urls": ["https://www.cs.umb.edu/~poneil/lsmtree.pdf"]
    },

    # =========================================================================
    # FACULTAD X: FINTECH, PAGOS & TRANSACCIONES SAGAS
    # =========================================================================
    {
        "faculty": "10_fintech_stripe_sagas",
        "filename": "1987_garcia_molina_sagas.pdf",
        "title": "Sagas: Distributed Long-Lived Transactions",
        "authors": ["Hector Garcia-Molina", "Kenneth Salem"],
        "year": 1987,
        "institution": "Princeton University / ACM SIGMOD",
        "urls": ["https://www.cs.cornell.edu/andru/cs711/2002fa/reading/sagas.pdf"]
    },

    # =========================================================================
    # FACULTAD XI: ZERO-TRUST, CRIPTOGRAFÍA DE CLAVE PÚBLICA & IDENTIDAD
    # =========================================================================
    {
        "faculty": "11_identidad_zerotrust_beyondcorp",
        "filename": "1976_diffie_hellman_new_directions_in_cryptography.pdf",
        "title": "New Directions in Cryptography (Public Key Cryptography & Key Exchange)",
        "authors": ["Whitfield Diffie", "Martin E. Hellman"],
        "year": 1976,
        "institution": "Stanford University / IEEE Transactions on Information Theory",
        "urls": [
            "https://ee.stanford.edu/~hellman/publications/24.pdf",
            "https://raw.githubusercontent.com/papers-we-love/papers-we-love/master/security/new-directions-in-cryptography.pdf"
        ]
    },
    {
        "faculty": "11_identidad_zerotrust_beyondcorp",
        "filename": "2020_nist_sp800_207_zero_trust.pdf",
        "title": "NIST SP 800-207: Zero Trust Architecture",
        "authors": ["Scott Rose", "Oliver Borchert", "Stu Mitchell", "Sean Connelly"],
        "year": 2020,
        "institution": "National Institute of Standards and Technology (NIST)",
        "urls": ["https://nvlpubs.nist.gov/nistpubs/SpecialPublications/NIST.SP.800-207.pdf"]
    },

    # =========================================================================
    # FACULTAD XII: SUPPLY CHAIN, SLSA & FIRMAS SIGSTORE
    # =========================================================================
    {
        "faculty": "12_supplychain_slsa_gitops",
        "filename": "2022_hinds_sigstore_software_signing.pdf",
        "title": "Sigstore: Software Signing for Everybody",
        "authors": ["Luke Hinds", "Dan Lorenc et al."],
        "year": 2022,
        "institution": "Linux Foundation / Google / ACM CCS",
        "urls": ["https://arxiv.org/pdf/2205.07628.pdf"]
    }
]

def download_paper(item: dict) -> bool:
    fac_dir = BASE_DIR / item["faculty"]
    fac_dir.mkdir(parents=True, exist_ok=True)
    
    target_file = fac_dir / item["filename"]
    meta_file = fac_dir / (target_file.stem + ".meta.json")
    
    # Escribir metadatos JSON
    meta_data = {
        "title": item["title"],
        "authors": item["authors"],
        "year": item["year"],
        "institution": item["institution"],
        "faculty": item["faculty"],
        "urls": item.get("urls", [])
    }
    meta_file.write_text(json.dumps(meta_data, indent=2, ensure_ascii=False), encoding="utf-8")
    
    # Si ya existe y es un PDF válido (> 10 KB), omitir descarga repetida
    if target_file.exists() and target_file.stat().st_size > 10240:
        print(f"  ✓ [En Caché] {item['filename']} ({target_file.stat().st_size / 1024:.1f} KB)")
        return True

    print(f"  ⬇️ Descargando [{item['faculty'].split('_')[0]}]: {item['title']}...")
    
    for url in item.get("urls", []):
        req = urllib.request.Request(
            url,
            headers={
                "User-Agent": "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
                "Accept": "application/pdf,application/xhtml+xml,text/html;q=0.9,*/*;q=0.8"
            }
        )
        try:
            with urllib.request.urlopen(req, timeout=25) as response:
                content = response.read()
                if len(content) > 5000:
                    with open(target_file, "wb") as f:
                        f.write(content)
                    print(f"  ✓ Descarga Completada: {item['filename']} ({len(content) / 1024:.1f} KB) desde {url}")
                    return True
        except Exception as e:
            print(f"    Falló URL {url}: {e}")
            continue

    print(f"  ❌ No se pudo descargar {item['filename']} de ninguna fuente.")
    return False

def main():
    print("====================================================================")
    print("  DESCARGADOR MASIVO DE PAPERS FUNDACIONALES Y AVANZADOS")
    print("====================================================================")
    
    successful = 0
    failed = 0
    
    for item in OPEN_ACCESS_PAPERS:
        ok = download_paper(item)
        if ok:
            successful += 1
        else:
            failed += 1
        time.sleep(0.3)

    print("--------------------------------------------------------------------")
    print(f"  Total Documentos Procesados : {successful} / {len(OPEN_ACCESS_PAPERS)}")
    print(f"  Total Fallos                 : {failed}")
    print("====================================================================")

    # Ingestar y destilar automáticamente con fitz (PyMuPDF)
    print("\n  Ejecutando Motor de Ingesta y Destilación Feynman sobre la biblioteca completa...")
    os.system("python3 /home/jaruiz/Desarrollo/scripts/ingest_and_distill_papers_feynman.py")

if __name__ == "__main__":
    main()
